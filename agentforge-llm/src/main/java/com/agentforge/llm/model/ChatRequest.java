package com.agentforge.llm.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * OpenAI Chat Completion 请求
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatRequest(
    String model,
    List<Message> messages,
    @JsonProperty("max_tokens") Integer maxTokens,
    Double temperature,
    Double topP,
    @JsonProperty("stream") Boolean stream,
    List<Tool> tools,
    @JsonProperty("tool_choice") Object toolChoice
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Message(
        String role,
        String content,
        String name,
        @JsonProperty("tool_calls") List<ToolCall> toolCalls,
        @JsonProperty("tool_call_id") String toolCallId,
        @JsonProperty("reasoning_content") String reasoningContent
    ) {
        public static Message system(String content) {
            return new Message("system", content, null, null, null, null);
        }

        public static Message user(String content) {
            return new Message("user", content, null, null, null, null);
        }

        public static Message assistant(String content) {
            return new Message("assistant", content, null, null, null, null);
        }

        public static Message assistant(String content, List<ToolCall> toolCalls) {
            return new Message("assistant", content, null, toolCalls, null, null);
        }

        public static Message tool(String toolCallId, String content) {
            return new Message("tool", content, null, null, toolCallId, null);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Tool(
        String type,
        Function function
    ) {
        public static Tool of(Function function) {
            return new Tool("function", function);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Function(
        String name,
        String description,
        Map<String, Object> parameters
    ) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ToolCall(
        String id,
        String type,
        FunctionCall function
    ) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record FunctionCall(
        String name,
        String arguments
    ) {}
}
