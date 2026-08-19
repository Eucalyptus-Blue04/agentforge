package com.agentforge.llm.client;

import com.agentforge.llm.model.ChatChunk;
import com.agentforge.llm.model.ChatRequest;
import com.agentforge.llm.model.ChatResponse;

import java.util.function.Consumer;

/**
 * LLM 客户端接口
 */
public interface LlmClient {

    /**
     * 同步调用 Chat Completion
     */
    ChatResponse chat(ChatRequest request);

    /**
     * 流式调用 Chat Completion（实时回调，边收边处理）
     */
    void streamChat(ChatRequest request, Consumer<ChatChunk> onChunk);
}
