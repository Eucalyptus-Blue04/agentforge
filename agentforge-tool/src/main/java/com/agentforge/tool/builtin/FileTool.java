package com.agentforge.tool.builtin;

import com.agentforge.tool.annotation.Tool;
import com.agentforge.tool.annotation.ToolParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

/**
 * 文件操作工具
 */
public class FileTool {

    @Tool(name = "read_file", description = "读取文件内容")
    public String readFile(
            @ToolParam(description = "文件路径") String filePath) {
        try {
            Path path = Path.of(filePath);
            if (!Files.exists(path)) {
                return "文件不存在: " + filePath;
            }
            return Files.readString(path);
        } catch (IOException e) {
            return "读取文件错误: " + e.getMessage();
        }
    }

    @Tool(name = "list_directory", description = "列出目录下的文件")
    public String listDirectory(
            @ToolParam(description = "目录路径") String dirPath) {
        try {
            Path path = Path.of(dirPath);
            if (!Files.isDirectory(path)) {
                return "不是有效目录: " + dirPath;
            }
            return Files.list(path)
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            return "列出目录错误: " + e.getMessage();
        }
    }
}
