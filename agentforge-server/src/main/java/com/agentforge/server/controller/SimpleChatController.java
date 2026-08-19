package com.agentforge.server.controller;

import com.agentforge.core.agent.AgentContext;
import com.agentforge.core.runtime.AgentRuntime;
import com.agentforge.core.runtime.StreamChunk;
import com.agentforge.server.entity.ChatMessage;
import com.agentforge.server.entity.ChatSession;
import com.agentforge.server.repository.ChatMessageRepository;
import com.agentforge.server.repository.ChatSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 聊天控制器 - 通过 AgentRuntime 执行对话，支持 SSE 流式输出和工具调用
 */
@RestController
@RequestMapping("/api/chat")
public class SimpleChatController {

    private static final Logger log = LoggerFactory.getLogger(SimpleChatController.class);

    private final AgentRuntime agentRuntime;
    private final ChatMessageRepository messageRepository;
    private final ChatSessionRepository sessionRepository;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public SimpleChatController(AgentRuntime agentRuntime,
                                 ChatMessageRepository messageRepository,
                                 ChatSessionRepository sessionRepository) {
        this.agentRuntime = agentRuntime;
        this.messageRepository = messageRepository;
        this.sessionRepository = sessionRepository;
    }

    /**
     * 非流式聊天（通过 AgentRuntime，支持工具调用）
     */
    @PostMapping
    public Map<String, Object> chat(@RequestBody Map<String, String> request) {
        String message = request.getOrDefault("message", "").trim();
        String sessionId = request.getOrDefault("sessionId", "default");
        String agentName = request.getOrDefault("agentName", "assistant");

        if (message.isEmpty()) {
            return Map.of("error", true, "message", "消息不能为空");
        }

        // 保存用户消息到数据库
        persistMessage(sessionId, agentName, "user", message);

        long startTime = System.currentTimeMillis();

        try {
            AgentContext context = new AgentContext(sessionId);
            var response = agentRuntime.execute(agentName, message, context);
            String content = response.content();

            if (content == null || content.isBlank()) {
                content = "（模型未返回有效内容，请重试）";
            }

            // 保存助手回复到数据库
            persistMessage(sessionId, agentName, "assistant", content);
            long duration = System.currentTimeMillis() - startTime;

            return Map.of("error", false, "content", content, "durationMs", duration, "sessionId", sessionId);
        } catch (Exception e) {
            log.error("Chat error: {}", e.getMessage(), e);
            return Map.of("error", true, "message", e.getMessage() != null ? e.getMessage() : "调用失败");
        }
    }

    /**
     * 流式聊天（SSE 实时推送，支持工具调用）
     */
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamChat(@RequestBody Map<String, String> request) {
        String message = request.getOrDefault("message", "").trim();
        String sessionId = request.getOrDefault("sessionId", "default");
        String agentName = request.getOrDefault("agentName", "assistant");

        SseEmitter emitter = new SseEmitter(180000L);
        java.util.concurrent.atomic.AtomicBoolean emitterActive = new java.util.concurrent.atomic.AtomicBoolean(true);

        emitter.onTimeout(() -> emitterActive.set(false));
        emitter.onError(e -> emitterActive.set(false));
        emitter.onCompletion(() -> emitterActive.set(false));

        if (message.isEmpty()) {
            try {
                emitter.send(SseEmitter.event().data(Map.of("error", true, "message", "消息不能为空")));
                emitter.complete();
            } catch (Exception ignored) {}
            return emitter;
        }

        // 保存用户消息到数据库
        persistMessage(sessionId, agentName, "user", message);

        executor.submit(() -> {
            try {
                StringBuilder fullContent = new StringBuilder();

                // 通过 AgentRuntime 流式执行（支持工具调用）
                AgentContext context = new AgentContext(sessionId);
                agentRuntime.streamExecute(agentName, message, context, chunk -> {
                    if (!emitterActive.get()) return;
                    try {
                        switch (chunk.type()) {
                            case "delta" -> {
                                fullContent.append(chunk.content());
                                emitter.send(SseEmitter.event().data(Map.of(
                                        "delta", chunk.content(),
                                        "done", false
                                )));
                            }
                            case "tool_call" -> {
                                emitter.send(SseEmitter.event().data(Map.of(
                                        "type", "tool_call",
                                        "toolName", chunk.toolName(),
                                        "toolCallId", chunk.toolCallId() != null ? chunk.toolCallId() : ""
                                )));
                            }
                            case "tool_result" -> {
                                emitter.send(SseEmitter.event().data(Map.of(
                                        "type", "tool_result",
                                        "toolName", chunk.toolName(),
                                        "result", chunk.content() != null ? chunk.content() : ""
                                )));
                            }
                            case "error" -> {
                                emitter.send(SseEmitter.event().data(Map.of(
                                        "error", true,
                                        "message", chunk.content() != null ? chunk.content() : "调用失败"
                                )));
                            }
                        }
                    } catch (Exception e) {
                        log.debug("SSE send failed: {}", e.getMessage());
                        emitterActive.set(false);
                    }
                });

                if (!emitterActive.get()) return;

                String result = fullContent.toString();
                if (result.isEmpty()) {
                    result = "（模型未返回有效内容，请重试）";
                }

                // 保存助手回复到数据库
                persistMessage(sessionId, agentName, "assistant", result);

                // 发送完成事件
                emitter.send(SseEmitter.event().data(Map.of(
                        "done", true,
                        "content", result,
                        "sessionId", sessionId
                )));
                emitter.complete();

                log.info("Stream chat completed, {} chars", result.length());
            } catch (Exception e) {
                if (emitterActive.get()) {
                    log.error("Stream chat error: {}", e.getMessage(), e);
                }
                if (emitterActive.getAndSet(false)) {
                    try {
                        emitter.send(SseEmitter.event().data(Map.of(
                                "error", true,
                                "message", e.getMessage() != null ? e.getMessage() : "调用失败"
                        )));
                    } catch (Exception ignored) {}
                    emitter.complete();
                }
            }
        });

        return emitter;
    }

    /**
     * 文件上传 - 提取文本内容（支持 Word/Excel/PPT/PDF/图片/文本）
     */
    @PostMapping("/upload")
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile file,
                                          @RequestParam(value = "sessionId", defaultValue = "default") String sessionId) {
        if (file.isEmpty()) {
            return Map.of("error", true, "message", "文件为空");
        }

        String filename = file.getOriginalFilename();
        String contentType = file.getContentType();
        long size = file.getSize();
        String ext = getExtension(filename);

        try {
            if (isWordFile(ext)) {
                String content = extractWordText(file);
                return fileResult(filename, size, "word", content);
            } else if (isExcelFile(ext)) {
                String content = extractExcelText(file);
                return fileResult(filename, size, "excel", content);
            } else if (isPptFile(ext)) {
                String content = extractPptText(file);
                return fileResult(filename, size, "ppt", content);
            } else if (isPdfFile(ext, contentType)) {
                String content = extractPdfText(file);
                return fileResult(filename, size, "pdf", content);
            } else if (isImageFile(contentType, ext)) {
                String content = extractImageText(file);
                byte[] bytes = file.getBytes();
                String base64 = java.util.Base64.getEncoder().encodeToString(bytes);
                return Map.of(
                        "error", false, "filename", nn(filename), "size", size,
                        "type", "image", "content", content, "base64", base64
                );
            } else {
                String content = extractTextFileContent(file);
                return fileResult(filename, size, "text", content);
            }
        } catch (Exception e) {
            log.error("File upload error: {}", e.getMessage(), e);
            return Map.of("error", true, "message", "文件处理失败: " + e.getMessage());
        }
    }

    private Map<String, Object> fileResult(String filename, long size, String type, String content) {
        return Map.of("error", false, "filename", nn(filename), "size", size, "type", type, "content", content);
    }

    /**
     * 带文件的流式聊天（支持 Word/Excel/PPT/PDF/图片/文本）
     */
    @PostMapping(value = "/stream-with-file", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamChatWithFile(@RequestParam("message") String message,
                                          @RequestParam(value = "file", required = false) MultipartFile[] files,
                                          @RequestParam(value = "sessionId", defaultValue = "default") String sessionId,
                                          @RequestParam(value = "agentName", defaultValue = "assistant") String agentName) {
        SseEmitter emitter = new SseEmitter(180000L);
        java.util.concurrent.atomic.AtomicBoolean emitterActive = new java.util.concurrent.atomic.AtomicBoolean(true);

        emitter.onTimeout(() -> emitterActive.set(false));
        emitter.onError(e -> emitterActive.set(false));
        emitter.onCompletion(() -> emitterActive.set(false));

        boolean hasFiles = files != null && files.length > 0 && files[0] != null && !files[0].isEmpty();
        if (message.isEmpty() && !hasFiles) {
            try {
                emitter.send(SseEmitter.event().data(Map.of("error", true, "message", "消息不能为空")));
                emitter.complete();
            } catch (Exception ignored) {}
            return emitter;
        }

        // 构建完整消息（包含文件提取的文本内容）
        String fullMessage;
        if (hasFiles) {
            try {
                StringBuilder fileSection = new StringBuilder();
                for (MultipartFile file : files) {
                    if (file == null || file.isEmpty()) continue;
                    String filename = file.getOriginalFilename();
                    String contentType = file.getContentType();
                    String ext = getExtension(filename);
                    String label = nn(filename);

                    if (isWordFile(ext)) {
                        String text = extractWordText(file);
                        fileSection.append("\n\n[用户上传了 Word 文档: ").append(label).append("]\n以下是文档内容：\n").append(text);
                    } else if (isExcelFile(ext)) {
                        String text = extractExcelText(file);
                        fileSection.append("\n\n[用户上传了 Excel 表格: ").append(label).append("]\n以下是表格内容：\n").append(text);
                    } else if (isPptFile(ext)) {
                        String text = extractPptText(file);
                        fileSection.append("\n\n[用户上传了 PPT 演示文稿: ").append(label).append("]\n以下是幻灯片内容：\n").append(text);
                    } else if (isPdfFile(ext, contentType)) {
                        String text = extractPdfText(file);
                        fileSection.append("\n\n[用户上传了 PDF 文件: ").append(label).append("]\n以下是 PDF 文档内容：\n").append(text);
                    } else if (isImageFile(contentType, ext)) {
                        String ocrText = extractImageText(file);
                        if (!ocrText.isEmpty()) {
                            fileSection.append("\n\n[用户上传了图片: ").append(label).append("]\n以下是从图片中识别出的文字：\n").append(ocrText);
                        } else {
                            fileSection.append("\n\n[用户上传了图片: ").append(label).append("，未能识别出文字内容]");
                        }
                    } else {
                        String text = extractTextFileContent(file);
                        fileSection.append("\n\n[用户上传了文件: ").append(label).append("]\n以下是文件内容：\n```\n").append(text).append("\n```");
                    }
                }
                fullMessage = message + fileSection;
            } catch (Exception e) {
                log.error("File processing error: {}", e.getMessage(), e);
                try {
                    emitter.send(SseEmitter.event().data(Map.of("error", true, "message", "文件处理失败: " + e.getMessage())));
                    emitter.complete();
                } catch (Exception ignored) {}
                return emitter;
            }
        } else {
            fullMessage = message;
        }

        // 保存用户消息到数据库
        persistMessage(sessionId, agentName, "user", fullMessage);

        executor.submit(() -> {
            try {
                StringBuilder fullContent = new StringBuilder();

                // 通过 AgentRuntime 流式执行（支持工具调用）
                AgentContext context = new AgentContext(sessionId);
                agentRuntime.streamExecute(agentName, fullMessage, context, chunk -> {
                    if (!emitterActive.get()) return;
                    try {
                        switch (chunk.type()) {
                            case "delta" -> {
                                fullContent.append(chunk.content());
                                emitter.send(SseEmitter.event().data(Map.of(
                                        "delta", chunk.content(),
                                        "done", false
                                )));
                            }
                            case "tool_call" -> {
                                emitter.send(SseEmitter.event().data(Map.of(
                                        "type", "tool_call",
                                        "toolName", chunk.toolName(),
                                        "toolCallId", chunk.toolCallId() != null ? chunk.toolCallId() : ""
                                )));
                            }
                            case "tool_result" -> {
                                emitter.send(SseEmitter.event().data(Map.of(
                                        "type", "tool_result",
                                        "toolName", chunk.toolName(),
                                        "result", chunk.content() != null ? chunk.content() : ""
                                )));
                            }
                            case "error" -> {
                                emitter.send(SseEmitter.event().data(Map.of(
                                        "error", true,
                                        "message", chunk.content() != null ? chunk.content() : "调用失败"
                                )));
                            }
                        }
                    } catch (Exception e) {
                        log.debug("SSE send failed: {}", e.getMessage());
                        emitterActive.set(false);
                    }
                });

                if (!emitterActive.get()) return;

                String result = fullContent.toString();
                if (result.isEmpty()) {
                    result = "（模型未返回有效内容，请重试）";
                }

                // 保存助手回复到数据库
                persistMessage(sessionId, agentName, "assistant", result);

                emitter.send(SseEmitter.event().data(Map.of(
                        "done", true,
                        "content", result,
                        "sessionId", sessionId
                )));
                emitter.complete();

                log.info("Stream chat with file completed, {} chars", result.length());
            } catch (Exception e) {
                if (emitterActive.get()) {
                    log.error("Stream chat with file error: {}", e.getMessage(), e);
                }
                if (emitterActive.getAndSet(false)) {
                    try {
                        emitter.send(SseEmitter.event().data(Map.of(
                                "error", true,
                                "message", e.getMessage() != null ? e.getMessage() : "调用失败"
                        )));
                    } catch (Exception ignored) {}
                    emitter.complete();
                }
            }
        });

        return emitter;
    }

    // ========== 文件类型判断 ==========

    private String getExtension(String filename) {
        if (filename == null) return "";
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot + 1).toLowerCase() : "";
    }

    private String nn(String s) { return s != null ? s : "unknown"; }

    private boolean isImageFile(String contentType, String ext) {
        if (contentType != null && contentType.startsWith("image/")) return true;
        return ext.matches("jpg|jpeg|png|gif|bmp|webp|tiff|tif");
    }

    private boolean isPdfFile(String ext, String contentType) {
        if ("pdf".equals(ext)) return true;
        return contentType != null && contentType.equals("application/pdf");
    }

    private boolean isWordFile(String ext) {
        return "doc".equals(ext) || "docx".equals(ext);
    }

    private boolean isExcelFile(String ext) {
        return "xls".equals(ext) || "xlsx".equals(ext);
    }

    private boolean isPptFile(String ext) {
        return "ppt".equals(ext) || "pptx".equals(ext);
    }

    // ========== 文本提取方法 ==========

    /** Word 文档提取 */
    private String extractWordText(MultipartFile file) throws IOException {
        try (InputStream is = file.getInputStream()) {
            StringBuilder sb = new StringBuilder();
            String ext = getExtension(file.getOriginalFilename());

            if ("docx".equals(ext)) {
                try (XWPFDocument doc = new XWPFDocument(is)) {
                    for (XWPFParagraph para : doc.getParagraphs()) {
                        String text = para.getText();
                        if (text != null && !text.isBlank()) {
                            sb.append(text).append("\n");
                        }
                    }
                    // 提取表格内容
                    doc.getTables().forEach(table -> {
                        table.getRows().forEach(row -> {
                            row.getTableCells().forEach(cell -> {
                                sb.append(cell.getText()).append("\t");
                            });
                            sb.append("\n");
                        });
                    });
                }
            } else {
                // .doc 格式
                try (HWPFDocument doc = new HWPFDocument(is);
                     WordExtractor extractor = new WordExtractor(doc)) {
                    sb.append(extractor.getText());
                }
            }

            String text = sb.toString().trim();
            return truncate(text, 50000);
        } catch (Exception e) {
            log.error("Word extraction error: {}", e.getMessage());
            throw new IOException("Word 文档解析失败: " + e.getMessage(), e);
        }
    }

    /** Excel 表格提取 */
    private String extractExcelText(MultipartFile file) throws IOException {
        try (InputStream is = file.getInputStream()) {
            StringBuilder sb = new StringBuilder();
            String ext = getExtension(file.getOriginalFilename());

            Workbook workbook = "xlsx".equals(ext) ? new XSSFWorkbook(is) : new HSSFWorkbook(is);
            try (workbook) {
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    Sheet sheet = workbook.getSheetAt(i);
                    sb.append("【").append(sheet.getSheetName()).append("】\n");

                    for (Row row : sheet) {
                        if (row == null) continue;
                        StringBuilder rowStr = new StringBuilder();
                        for (int c = 0; c < row.getLastCellNum(); c++) {
                            Cell cell = row.getCell(c);
                            if (c > 0) rowStr.append("\t");
                            rowStr.append(getCellStringValue(cell));
                        }
                        String line = rowStr.toString().trim();
                        if (!line.isEmpty()) {
                            sb.append(line).append("\n");
                        }
                    }
                    sb.append("\n");
                }
            }

            String text = sb.toString().trim();
            return truncate(text, 50000);
        } catch (Exception e) {
            log.error("Excel extraction error: {}", e.getMessage());
            throw new IOException("Excel 表格解析失败: " + e.getMessage(), e);
        }
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                // 避免科学计数法
                double val = cell.getNumericCellValue();
                if (val == Math.floor(val) && !Double.isInfinite(val)) {
                    return String.valueOf((long) val);
                }
                return String.valueOf(val);
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try { return cell.getStringCellValue(); } catch (Exception e1) {
                    try { return String.valueOf(cell.getNumericCellValue()); } catch (Exception e2) {
                        return cell.getCellFormula();
                    }
                }
            default: return "";
        }
    }

    /** PPT 演示文稿提取 */
    private String extractPptText(MultipartFile file) throws IOException {
        try (InputStream is = file.getInputStream();
             XMLSlideShow ppt = new XMLSlideShow(is)) {
            StringBuilder sb = new StringBuilder();
            int slideNum = 0;

            for (XSLFSlide slide : ppt.getSlides()) {
                slideNum++;
                sb.append("【第 ").append(slideNum).append(" 页】\n");

                for (XSLFShape shape : slide.getShapes()) {
                    if (shape instanceof XSLFTextShape) {
                        String text = ((XSLFTextShape) shape).getText();
                        if (text != null && !text.isBlank()) {
                            sb.append(text).append("\n");
                        }
                    }
                }
                sb.append("\n");
            }

            String text = sb.toString().trim();
            return truncate(text, 50000);
        } catch (Exception e) {
            log.error("PPT extraction error: {}", e.getMessage());
            throw new IOException("PPT 演示文稿解析失败: " + e.getMessage(), e);
        }
    }

    /** PDF 文本提取 */
    private String extractPdfText(MultipartFile file) throws IOException {
        try (InputStream is = file.getInputStream();
             PDDocument document = Loader.loadPDF(is.readAllBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            return truncate(text.trim(), 50000);
        } catch (Exception e) {
            log.error("PDF extraction error: {}", e.getMessage());
            throw new IOException("PDF 解析失败: " + e.getMessage(), e);
        }
    }

    /** 图片 OCR 文字识别 */
    private String extractImageText(MultipartFile file) {
        try {
            byte[] imageBytes = file.getBytes();
            Path tempFile = Files.createTempFile("ocr_", ".png");
            try {
                Files.write(tempFile, imageBytes);
                Tesseract tesseract = new Tesseract();

                // 设置 tessdata 路径（优先项目根目录，其次系统安装目录）
                Path localTessData = Path.of("tessdata");
                Path systemTessData = Path.of("C:", "Program Files", "Tesseract-OCR", "tessdata");
                if (Files.isDirectory(localTessData)) {
                    tesseract.setDatapath(localTessData.toAbsolutePath().toString());
                } else if (Files.isDirectory(systemTessData)) {
                    tesseract.setDatapath(systemTessData.toAbsolutePath().toString());
                }

                tesseract.setLanguage("chi_sim+eng");
                String result = tesseract.doOCR(tempFile.toFile());
                return truncate(result.trim(), 30000);
            } finally {
                Files.deleteIfExists(tempFile);
            }
        } catch (TesseractException e) {
            log.warn("OCR failed (Tesseract not installed?): {}", e.getMessage());
            return extractImageMetadata(file);
        } catch (Exception e) {
            log.warn("Image processing error: {}", e.getMessage());
            return extractImageMetadata(file);
        }
    }

    /** 提取图片元信息（OCR 不可用时的回退方案） */
    private String extractImageMetadata(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            BufferedImage img = ImageIO.read(is);
            if (img != null) {
                return "[图片信息: " + img.getWidth() + "x" + img.getHeight() + " 像素, 格式: " + getExtension(file.getOriginalFilename()) + "]";
            }
        } catch (Exception ignored) {}
        return "";
    }

    /** 文本文件提取 */
    private String extractTextFileContent(MultipartFile file) throws IOException {
        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        return truncate(content, 50000);
    }

    private String truncate(String text, int maxLen) {
        if (text.length() > maxLen) {
            return text.substring(0, maxLen) + "\n...(内容过长，已截断)";
        }
        return text;
    }

    /**
     * 保存消息到数据库（持久化）
     */
    private void persistMessage(String sessionId, String agentName, String role, String content) {
        try {
            // 保存或更新会话
            ChatSession session = sessionRepository.findBySessionId(sessionId)
                    .orElseGet(() -> {
                        ChatSession s = new ChatSession();
                        s.setSessionId(sessionId);
                        s.setAgentName(agentName);
                        s.setTitle(content.substring(0, Math.min(content.length(), 50)));
                        return s;
                    });
            session.setMessageCount(session.getMessageCount() + 1);
            session.setAgentName(agentName);
            sessionRepository.save(session);

            // 保存消息
            ChatMessage msg = new ChatMessage();
            msg.setSessionId(sessionId);
            msg.setAgentName(agentName);
            msg.setRole(role);
            msg.setContent(content);
            messageRepository.save(msg);
        } catch (Exception e) {
            log.warn("Failed to persist message: {}", e.getMessage());
        }
    }

    /**
     * 获取所有会话列表
     */
    @GetMapping("/sessions")
    public List<Map<String, Object>> listSessions() {
        return sessionRepository.findAllByOrderByUpdatedAtDesc().stream()
                .map(s -> {
                    Map<String, Object> m = new java.util.HashMap<>();
                    m.put("sessionId", s.getSessionId());
                    m.put("agentName", s.getAgentName());
                    m.put("title", s.getTitle());
                    m.put("messageCount", s.getMessageCount());
                    m.put("createdAt", s.getCreatedAt().toString());
                    m.put("updatedAt", s.getUpdatedAt().toString());
                    return m;
                })
                .toList();
    }

    /**
     * 获取指定会话的消息历史
     */
    @GetMapping("/sessions/{sessionId}/messages")
    public List<Map<String, Object>> getSessionMessages(@PathVariable String sessionId) {
        return messageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId).stream()
                .map(msg -> {
                    Map<String, Object> m = new java.util.HashMap<>();
                    m.put("role", msg.getRole());
                    m.put("content", msg.getContent());
                    m.put("createdAt", msg.getCreatedAt().toString());
                    return m;
                })
                .toList();
    }

    /**
     * 删除会话
     */
    @DeleteMapping("/sessions/{sessionId}")
    public Map<String, String> deleteSession(@PathVariable String sessionId) {
        sessionRepository.findBySessionId(sessionId).ifPresent(sessionRepository::delete);
        List<ChatMessage> messages = messageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId);
        messageRepository.deleteAll(messages);
        return Map.of("status", "ok");
    }
}
