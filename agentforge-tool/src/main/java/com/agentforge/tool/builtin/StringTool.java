package com.agentforge.tool.builtin;

import com.agentforge.tool.annotation.Tool;
import com.agentforge.tool.annotation.ToolParam;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理工具
 */
public class StringTool {

    @Tool(name = "string_replace", description = "字符串替换")
    public String stringReplace(
            @ToolParam(description = "原始字符串") String text,
            @ToolParam(description = "要替换的内容") String target,
            @ToolParam(description = "替换为") String replacement) {
        if (text == null) return "";
        return text.replace(target, replacement);
    }

    @Tool(name = "string_contains", description = "检查字符串是否包含指定内容")
    public String stringContains(
            @ToolParam(description = "原始字符串") String text,
            @ToolParam(description = "要查找的内容") String search) {
        if (text == null) return "false";
        return String.valueOf(text.contains(search));
    }

    @Tool(name = "regex_match", description = "正则表达式匹配")
    public String regexMatch(
            @ToolParam(description = "原始字符串") String text,
            @ToolParam(description = "正则表达式") String regex) {
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            java.util.List<String> matches = new java.util.ArrayList<>();
            while (matcher.find()) {
                matches.add(matcher.group());
            }
            return matches.isEmpty() ? "未找到匹配" : String.join(", ", matches);
        } catch (Exception e) {
            return "正则错误: " + e.getMessage();
        }
    }

    @Tool(name = "string_split", description = "字符串分割")
    public String stringSplit(
            @ToolParam(description = "原始字符串") String text,
            @ToolParam(description = "分隔符") String delimiter) {
        if (text == null) return "";
        String[] parts = text.split(java.util.regex.Pattern.quote(delimiter));
        return String.join("\n", parts);
    }

    @Tool(name = "string_to_uppercase", description = "转大写")
    public String toUpperCase(@ToolParam(description = "原始字符串") String text) {
        return text != null ? text.toUpperCase() : "";
    }

    @Tool(name = "string_to_lowercase", description = "转小写")
    public String toLowerCase(@ToolParam(description = "原始字符串") String text) {
        return text != null ? text.toLowerCase() : "";
    }

    @Tool(name = "string_length", description = "获取字符串长度")
    public String stringLength(@ToolParam(description = "原始字符串") String text) {
        return text != null ? String.valueOf(text.length()) : "0";
    }

    @Tool(name = "string_trim", description = "去除首尾空白")
    public String trim(@ToolParam(description = "原始字符串") String text) {
        return text != null ? text.trim() : "";
    }
}
