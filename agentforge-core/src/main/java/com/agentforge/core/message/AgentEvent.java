package com.agentforge.core.message;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Agent 事件 - 用于 Agent 间通信和状态通知
 */
public record AgentEvent(
    String id,
    String agentName,
    EventType type,
    Instant timestamp,
    Map<String, Object> data
) {
    public AgentEvent {
        if (id == null) id = UUID.randomUUID().toString();
        if (timestamp == null) timestamp = Instant.now();
    }

    public enum EventType {
        AGENT_CREATED,
        AGENT_STARTED,
        AGENT_COMPLETED,
        AGENT_ERROR,
        MESSAGE_SENT,
        MESSAGE_RECEIVED,
        TOOL_CALLED,
        TOOL_RESULT,
        WORKFLOW_STARTED,
        WORKFLOW_STEP,
        WORKFLOW_COMPLETED,
        CUSTOM
    }

    public static AgentEvent of(String agentName, EventType type) {
        return new AgentEvent(null, agentName, type, null, Map.of());
    }

    public static AgentEvent of(String agentName, EventType type, Map<String, Object> data) {
        return new AgentEvent(null, agentName, type, null, data);
    }
}
