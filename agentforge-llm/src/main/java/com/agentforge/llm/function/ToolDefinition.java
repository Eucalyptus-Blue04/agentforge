package com.agentforge.llm.function;

import java.util.Map;

/**
 * 工具定义 - 用于 Function Calling
 */
public record ToolDefinition(
    String name,
    String description,
    Map<String, Object> parameters
) {}
