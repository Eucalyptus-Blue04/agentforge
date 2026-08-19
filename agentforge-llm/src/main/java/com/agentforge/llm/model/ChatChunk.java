package com.agentforge.llm.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * OpenAI 流式响应块
 */
public record ChatChunk(
    String id,
    String object,
    Long created,
    String model,
    List<Choice> choices
) {
    public record Choice(
        Integer index,
        Delta delta,
        @JsonProperty("finish_reason") String finishReason
    ) {}

    public record Delta(
        String role,
        String content,
        @JsonProperty("tool_calls") List<ToolCallDelta> toolCalls,
        @JsonProperty("reasoning_content") String reasoningContent
    ) {}

    public record ToolCallDelta(
        Integer index,
        String id,
        String type,
        FunctionCall function
    ) {}

    public record FunctionCall(
        String name,
        String arguments
    ) {}

    public String deltaContent() {
        if (choices == null || choices.isEmpty()) return null;
        Delta delta = choices.get(0).delta();
        return delta != null ? delta.content() : null;
    }

    public String deltaReasoningContent() {
        if (choices == null || choices.isEmpty()) return null;
        Delta delta = choices.get(0).delta();
        return delta != null ? delta.reasoningContent() : null;
    }
}
