package com.agentforge.server.dto;

import java.util.Map;

/**
 * 工作流执行请求
 */
public record WorkflowRequest(
    String message,
    Map<String, Object> variables
) {}
