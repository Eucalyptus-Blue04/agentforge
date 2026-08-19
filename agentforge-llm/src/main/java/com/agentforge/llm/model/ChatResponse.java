package com.agentforge.llm.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * OpenAI Chat Completion 响应
 */
public record ChatResponse(
    String id,
    String object,
    Long created,
    String model,
    List<Choice> choices,
    Usage usage
) {
    public record Choice(
        Integer index,
        ChatRequest.Message message,
        @JsonProperty("finish_reason") String finishReason
    ) {}

    public record Usage(
        @JsonProperty("prompt_tokens") Integer promptTokens,
        @JsonProperty("completion_tokens") Integer completionTokens,
        @JsonProperty("total_tokens") Integer totalTokens
    ) {}

    /**
     * 获取第一个回复内容
     */
    public String firstContent() {
        if (choices == null || choices.isEmpty()) return null;
        ChatRequest.Message msg = choices.get(0).message();
        return msg != null ? msg.content() : null;
    }

    /**
     * 获取第一个 tool_calls
     */
    public List<ChatRequest.ToolCall> firstToolCalls() {
        if (choices == null || choices.isEmpty()) return null;
        ChatRequest.Message msg = choices.get(0).message();
        return msg != null ? msg.toolCalls() : null;
    }
}
