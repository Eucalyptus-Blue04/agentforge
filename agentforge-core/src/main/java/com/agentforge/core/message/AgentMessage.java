package com.agentforge.core.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.Instant;

/**
 * Agent 消息基类 - 所有 Agent 间通信的消息都实现此接口
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = UserMessage.class, name = "user"),
    @JsonSubTypes.Type(value = AssistantMessage.class, name = "assistant"),
    @JsonSubTypes.Type(value = SystemMessage.class, name = "system"),
    @JsonSubTypes.Type(value = ToolCallMessage.class, name = "tool_call"),
    @JsonSubTypes.Type(value = ToolResultMessage.class, name = "tool_result")
})
public sealed interface AgentMessage permits
        UserMessage, AssistantMessage, SystemMessage,
        ToolCallMessage, ToolResultMessage {

    String id();
    String agentName();
    Instant timestamp();
    MessageType type();

    enum MessageType {
        USER, ASSISTANT, SYSTEM, TOOL_CALL, TOOL_RESULT
    }
}
