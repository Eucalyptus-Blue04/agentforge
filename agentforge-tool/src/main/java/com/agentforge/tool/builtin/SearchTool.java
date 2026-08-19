package com.agentforge.tool.builtin;

import com.agentforge.tool.annotation.Tool;
import com.agentforge.tool.annotation.ToolParam;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 搜索工具
 * <p>
 * 使用 Bing（必应）进行网页搜索，使用百度百科进行知识查询
 */
public class SearchTool {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    @Tool(name = "web_search", description = "搜索互联网获取信息，返回搜索结果摘要")
    public String webSearch(
            @ToolParam(description = "搜索关键词") String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String url = "https://www.bing.com/search?q=" + encodedQuery + "&mkt=zh-CN";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                    .GET()
                    .timeout(Duration.ofSeconds(15))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parseBingResult(response.body());
            } else {
                return "搜索失败: HTTP " + response.statusCode();
            }
        } catch (Exception e) {
            return "搜索错误: " + e.getMessage();
        }
    }

    @Tool(name = "baike_search", description = "在百度百科中搜索条目，获取知识性信息")
    public String baikeSearch(
            @ToolParam(description = "搜索关键词") String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String url = "https://baike.baidu.com/api/openapi/BaikeLemmaCardApi?scope=103&format=json&appid=379020&bk_key=" + encodedQuery + "&bk_length=600";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .header("Accept", "application/json")
                    .GET()
                    .timeout(Duration.ofSeconds(15))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parseBaikeResult(response.body());
            } else {
                return "百度百科未找到相关条目: " + query;
            }
        } catch (Exception e) {
            return "搜索错误: " + e.getMessage();
        }
    }

    /**
     * 解析 Bing 搜索结果 HTML
     */
    private String parseBingResult(String html) {
        try {
            StringBuilder result = new StringBuilder();
            List<String[]> items = new ArrayList<>();

            // 提取搜索结果条目：<li class="b_algo"> 下的 <h2><a> 标题和 <p>/<div class="b_caption"> 摘要
            Pattern itemPattern = Pattern.compile(
                    "<li class=\"b_algo\"[^>]*>(.*?)</li>",
                    Pattern.DOTALL
            );
            Matcher itemMatcher = itemPattern.matcher(html);

            while (itemMatcher.find() && items.size() < 5) {
                String block = itemMatcher.group(1);

                // 提取标题
                String title = "";
                Pattern titlePattern = Pattern.compile("<h2[^>]*>\\s*<a[^>]*>(.*?)</a>", Pattern.DOTALL);
                Matcher titleMatcher = titlePattern.matcher(block);
                if (titleMatcher.find()) {
                    title = stripHtml(titleMatcher.group(1));
                }

                // 提取摘要
                String snippet = "";
                // 尝试 <p> 标签
                Pattern snippetPattern = Pattern.compile("<p[^>]*>(.*?)</p>", Pattern.DOTALL);
                Matcher snippetMatcher = snippetPattern.matcher(block);
                if (snippetMatcher.find()) {
                    snippet = stripHtml(snippetMatcher.group(1));
                }
                // 如果 <p> 没有内容，尝试 b_caption
                if (snippet.isEmpty()) {
                    Pattern captionPattern = Pattern.compile("<div class=\"b_caption\"[^>]*>(.*?)</div>", Pattern.DOTALL);
                    Matcher captionMatcher = captionPattern.matcher(block);
                    if (captionMatcher.find()) {
                        snippet = stripHtml(captionMatcher.group(1));
                    }
                }

                if (!title.isEmpty()) {
                    items.add(new String[]{title, snippet});
                }
            }

            if (items.isEmpty()) {
                // 备用：尝试提取 <ol id="b_results"> 中的结果
                return "未找到相关搜索结果，请尝试换个关键词搜索";
            }

            for (int i = 0; i < items.size(); i++) {
                String[] item = items.get(i);
                result.append(i + 1).append(". ").append(item[0]).append("\n");
                if (!item[1].isEmpty()) {
                    result.append("   ").append(truncate(item[1], 200)).append("\n");
                }
                result.append("\n");
            }

            return result.toString().trim();
        } catch (Exception e) {
            return "解析搜索结果出错: " + e.getMessage();
        }
    }

    /**
     * 解析百度百科 API 返回的 JSON
     */
    private String parseBaikeResult(String json) {
        try {
            StringBuilder result = new StringBuilder();

            // 提取标题
            String title = extractField(json, "title");
            if (title != null) {
                result.append("标题: ").append(title).append("\n\n");
            }

            // 提取摘要 / 描述
            String abstractText = extractField(json, "abstract");
            if (abstractText != null && !abstractText.isEmpty()) {
                result.append("简介: ").append(truncate(abstractText, 1000)).append("\n");
            }

            // 提取描述
            String desc = extractField(json, "desc");
            if (desc != null && !desc.isEmpty() && !desc.equals(abstractText)) {
                result.append("\n详细: ").append(truncate(desc, 500)).append("\n");
            }

            return result.length() > 0 ? result.toString() : "未找到相关百科条目";
        } catch (Exception e) {
            return "解析错误: " + e.getMessage();
        }
    }

    /**
     * 去除 HTML 标签
     */
    private String stripHtml(String html) {
        if (html == null) return "";
        return html.replaceAll("<[^>]+>", "")
                .replaceAll("&nbsp;", " ")
                .replaceAll("&amp;", "&")
                .replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("&quot;", "\"")
                .replaceAll("&#39;", "'")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private String extractField(String json, String fieldName) {
        // 用 indexOf 替代正则，避免栈溢出
        String key = "\"" + fieldName + "\":\"";
        int start = json.indexOf(key);
        if (start < 0) return null;
        start += key.length();

        // 找到未转义的结束引号
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '\\' && i + 1 < json.length()) {
                char next = json.charAt(i + 1);
                if (next == '"') { sb.append('"'); i++; continue; }
                if (next == 'n') { sb.append('\n'); i++; continue; }
                if (next == '\\') { sb.append('\\'); i++; continue; }
                sb.append(c);
            } else if (c == '"') {
                break;
            } else {
                sb.append(c);
            }
        }
        return sb.length() > 0 ? sb.toString() : null;
    }

    private String truncate(String s, int maxLen) {
        if (s == null) return "";
        return s.length() > maxLen ? s.substring(0, maxLen) + "..." : s;
    }
}
