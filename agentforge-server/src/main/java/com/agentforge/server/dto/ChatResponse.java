package com.agentforge.server.dto;

/**
 * 聊天响应
 */
public record ChatResponse(
    String content,
    String finishReason,
    int iterations,
    long durationMs,
    String sessionId
) {}
