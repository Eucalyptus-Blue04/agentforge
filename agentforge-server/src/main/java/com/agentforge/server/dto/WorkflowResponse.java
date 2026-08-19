package com.agentforge.server.dto;

import java.util.List;
import java.util.Map;

/**
 * 工作流执行响应
 */
public record WorkflowResponse(
    String workflowName,
    String finalOutput,
    List<Map<String, Object>> executionSteps,
    long durationMs,
    int totalIterations
) {}
