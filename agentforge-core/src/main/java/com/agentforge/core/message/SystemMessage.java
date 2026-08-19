package com.agentforge.core.message;

import java.time.Instant;
import java.util.UUID;

/**
 * 系统消息 (System Prompt)
 */
public record SystemMessage(
    String id,
    String agentName,
    Instant timestamp,
    String content
) implements AgentMessage {

    public SystemMessage {
        if (id == null) id = UUID.randomUUID().toString();
        if (timestamp == null) timestamp = Instant.now();
    }

    public SystemMessage(String agentName, String content) {
        this(null, agentName, null, content);
    }

    @Override
    public MessageType type() {
        return MessageType.SYSTEM;
    }
}
