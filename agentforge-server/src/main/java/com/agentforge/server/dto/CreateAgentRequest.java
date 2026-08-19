package com.agentforge.server.dto;

import java.util.List;

/**
 * 创建 Agent 请求
 */
public record CreateAgentRequest(
    String name,
    String description,
    String systemPrompt,
    List<String> tools,
    Double temperature,
    Integer maxTokens,
    String modelName
) {}
