package com.agentforge.core.message;

import java.time.Instant;
import java.util.UUID;

/**
 * 工具执行结果消息
 */
public record ToolResultMessage(
    String id,
    String agentName,
    Instant timestamp,
    String toolCallId,
    String toolName,
    String result,
    boolean isError
) implements AgentMessage {

    public ToolResultMessage {
        if (id == null) id = UUID.randomUUID().toString();
        if (timestamp == null) timestamp = Instant.now();
    }

    public ToolResultMessage(String agentName, String toolCallId, String toolName, String result) {
        this(null, agentName, null, toolCallId, toolName, result, false);
    }

    public static ToolResultMessage error(String agentName, String toolCallId, String toolName, String error) {
        return new ToolResultMessage(null, agentName, null, toolCallId, toolName, error, true);
    }

    @Override
    public MessageType type() {
        return MessageType.TOOL_RESULT;
    }
}
