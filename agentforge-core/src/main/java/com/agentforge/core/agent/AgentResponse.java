package com.agentforge.core.agent;

import com.agentforge.core.message.AssistantMessage;

import java.util.List;
import java.util.Map;

/**
 * Agent 响应结果
 */
public record AgentResponse(
    String content,
    String finishReason,
    List<AssistantMessage.ToolCall> toolCalls,
    Map<String, Object> metadata,
    int iterations,
    long durationMs
) {
    public AgentResponse {
        if (metadata == null) metadata = Map.of();
    }

    public static AgentResponse of(String content) {
        return new AgentResponse(content, "stop", List.of(), Map.of(), 1, 0);
    }

    public static AgentResponse withToolCalls(String content, List<AssistantMessage.ToolCall> toolCalls) {
        return new AgentResponse(content, "tool_calls", toolCalls, Map.of(), 1, 0);
    }

    public boolean hasToolCalls() {
        return toolCalls != null && !toolCalls.isEmpty();
    }
}
