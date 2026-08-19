package com.agentforge.llm.function;

import com.agentforge.llm.model.ChatRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Function Calling 处理器
 * <p>
 * 将工具定义转换为 OpenAI Function Calling 格式
 */
public class FunctionCallHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将工具定义列表转换为 OpenAI Tool 列表
     */
    public List<ChatRequest.Tool> toToolDefinitions(List<ToolDefinition> tools) {
        if (tools == null || tools.isEmpty()) {
            return List.of();
        }

        return tools.stream()
                .map(tool -> ChatRequest.Tool.of(
                        new ChatRequest.Function(
                                tool.name(),
                                tool.description(),
                                tool.parameters()
                        )
                ))
                .collect(Collectors.toList());
    }

    /**
     * 将 Tool 转换为 Function Calling 格式
     */
    public ChatRequest.Tool toTool(ToolDefinition tool) {
        return ChatRequest.Tool.of(
                new ChatRequest.Function(
                        tool.name(),
                        tool.description(),
                        tool.parameters()
                )
        );
    }

    /**
     * 解析 Function Call 的参数 JSON
     */
    public Map<String, Object> parseArguments(String argumentsJson) {
        try {
            return objectMapper.readValue(argumentsJson, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse function arguments: " + argumentsJson, e);
        }
    }
}
