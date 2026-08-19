package com.agentforge.core.runtime;

/**
 * 流式执行的输出块
 */
public record StreamChunk(
    String type,      // "delta", "tool_call", "tool_result", "done", "error"
    String content,   // 文本内容或工具结果
    String toolName,  // 工具名称（仅 tool_call/tool_result）
    String toolCallId // 工具调用ID（仅 tool_call/tool_result）
) {
    public static StreamChunk delta(String content) {
        return new StreamChunk("delta", content, null, null);
    }

    public static StreamChunk toolCall(String id, String name) {
        return new StreamChunk("tool_call", null, name, id);
    }

    public static StreamChunk toolResult(String id, String name, String result) {
        return new StreamChunk("tool_result", result, name, id);
    }

    public static StreamChunk done() {
        return new StreamChunk("done", null, null, null);
    }

    public static StreamChunk error(String msg) {
        return new StreamChunk("error", msg, null, null);
    }
}
