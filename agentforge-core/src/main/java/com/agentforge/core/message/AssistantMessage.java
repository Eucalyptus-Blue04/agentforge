package com.agentforge.core.message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Agent 回复的消息
 */
public record AssistantMessage(
    String id,
    String agentName,
    Instant timestamp,
    String content,
    List<ToolCall> toolCalls,
    String finishReason
) implements AgentMessage {

    public AssistantMessage {
        if (id == null) id = UUID.randomUUID().toString();
        if (timestamp == null) timestamp = Instant.now();
    }

    public AssistantMessage(String agentName, String content) {
        this(null, agentName, null, content, null, "stop");
    }

    public AssistantMessage(String agentName, String content, List<ToolCall> toolCalls) {
        this(null, agentName, null, content, toolCalls, "tool_calls");
    }

    @Override
    public MessageType type() {
        return MessageType.ASSISTANT;
    }

    /**
     * Function Calling 中的工具调用
     */
    public record ToolCall(
        String id,
        String functionName,
        String arguments
    ) {}
}
