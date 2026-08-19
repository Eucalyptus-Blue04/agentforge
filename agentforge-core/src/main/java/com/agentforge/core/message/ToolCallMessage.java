package com.agentforge.core.message;

import java.time.Instant;
import java.util.UUID;

/**
 * 工具调用请求消息
 */
public record ToolCallMessage(
    String id,
    String agentName,
    Instant timestamp,
    String toolCallId,
    String toolName,
    String arguments
) implements AgentMessage {

    public ToolCallMessage {
        if (id == null) id = UUID.randomUUID().toString();
        if (timestamp == null) timestamp = Instant.now();
    }

    public ToolCallMessage(String agentName, String toolCallId, String toolName, String arguments) {
        this(null, agentName, null, toolCallId, toolName, arguments);
    }

    @Override
    public MessageType type() {
        return MessageType.TOOL_CALL;
    }
}
