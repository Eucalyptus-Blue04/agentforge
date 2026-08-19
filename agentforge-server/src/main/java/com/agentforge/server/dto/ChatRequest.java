package com.agentforge.server.dto;

import java.util.List;

/**
 * 聊天请求
 */
public record ChatRequest(
    String message,
    String sessionId,
    List<String> context
) {}
