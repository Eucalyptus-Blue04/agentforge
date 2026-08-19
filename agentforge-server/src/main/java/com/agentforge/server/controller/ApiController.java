package com.agentforge.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * API 健康检查
 */
@RestController
public class ApiController {

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }

    @GetMapping("/api/info")
    public Map<String, Object> info() {
        return Map.of(
                "name", "AgentForge",
                "version", "1.0.0",
                "description", "AI Agent Collaboration Platform",
                "endpoints", Map.of(
                        "agents", "/api/agents",
                        "workflows", "/api/workflows",
                        "websocket", "/ws/agents"
                )
        );
    }
}
