package com.agentforge.core.message;

import java.time.Instant;
import java.util.UUID;

/**
 * 用户发送的消息
 */
public record UserMessage(
    String id,
    String agentName,
    Instant timestamp,
    String content
) implements AgentMessage {

    public UserMessage {
        if (id == null) id = UUID.randomUUID().toString();
        if (timestamp == null) timestamp = Instant.now();
    }

    public UserMessage(String agentName, String content) {
        this(null, agentName, null, content);
    }

    @Override
    public MessageType type() {
        return MessageType.USER;
    }
}
