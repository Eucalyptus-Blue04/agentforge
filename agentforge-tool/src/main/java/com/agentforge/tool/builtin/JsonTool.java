package com.agentforge.tool.builtin;

import com.agentforge.tool.annotation.Tool;
import com.agentforge.tool.annotation.ToolParam;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Map;

/**
 * JSON 处理工具
 */
public class JsonTool {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    @Tool(name = "parse_json", description = "解析 JSON 字符串并格式化输出")
    public String parseJson(
            @ToolParam(description = "JSON 字符串") String json) {
        try {
            JsonNode node = objectMapper.readTree(json);
            return objectMapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            return "JSON 解析错误: " + e.getMessage();
        }
    }

    @Tool(name = "extract_json_field", description = "从 JSON 中提取指定字段的值")
    public String extractJsonField(
            @ToolParam(description = "JSON 字符串") String json,
            @ToolParam(description = "字段路径，例如: data.name, items[0].id") String fieldPath) {
        try {
            JsonNode node = objectMapper.readTree(json);
            String[] parts = fieldPath.split("\\.");
            JsonNode current = node;

            for (String part : parts) {
                if (current == null) return "null";

                // 处理数组索引: items[0]
                if (part.contains("[") && part.contains("]")) {
                    String arrayName = part.substring(0, part.indexOf("["));
                    int index = Integer.parseInt(part.substring(part.indexOf("[") + 1, part.indexOf("]")));
                    current = current.get(arrayName);
                    if (current != null && current.isArray()) {
                        current = current.get(index);
                    }
                } else {
                    current = current.get(part);
                }
            }

            if (current == null) return "null";
            return current.isTextual() ? current.asText() : current.toString();
        } catch (Exception e) {
            return "提取错误: " + e.getMessage();
        }
    }

    @Tool(name = "create_json", description = "创建 JSON 对象")
    public String createJson(
            @ToolParam(description = "键值对，格式: key1=value1,key2=value2") String keyValuePairs) {
        try {
            Map<String, String> map = new java.util.LinkedHashMap<>();
            for (String pair : keyValuePairs.split(",")) {
                String[] kv = pair.trim().split("=", 2);
                if (kv.length == 2) {
                    map.put(kv[0].trim(), kv[1].trim());
                }
            }
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            return "创建错误: " + e.getMessage();
        }
    }

    @Tool(name = "validate_json", description = "验证 JSON 字符串是否有效")
    public String validateJson(
            @ToolParam(description = "JSON 字符串") String json) {
        try {
            objectMapper.readTree(json);
            return "JSON 格式有效";
        } catch (JsonProcessingException e) {
            return "JSON 格式无效: " + e.getMessage();
        }
    }
}
