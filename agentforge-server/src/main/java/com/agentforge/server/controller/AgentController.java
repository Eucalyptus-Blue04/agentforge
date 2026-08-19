package com.agentforge.server.controller;

import com.agentforge.core.agent.AgentConfig;
import com.agentforge.core.runtime.AgentRuntime;
import com.agentforge.server.dto.CreateAgentRequest;
import com.agentforge.server.entity.ChatMessage;
import com.agentforge.server.entity.ChatSession;
import com.agentforge.server.repository.ChatMessageRepository;
import com.agentforge.server.repository.ChatSessionRepository;
import com.agentforge.server.repository.AgentMetricRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Agent REST API
 */
@RestController
@RequestMapping("/api/agents")

public class AgentController {

    private final AgentRuntime agentRuntime;
    private final ChatSessionRepository sessionRepository;
    private final ChatMessageRepository messageRepository;
    private final AgentMetricRepository metricRepository;

    public AgentController(AgentRuntime agentRuntime,
                           ChatSessionRepository sessionRepository,
                           ChatMessageRepository messageRepository,
                           AgentMetricRepository metricRepository) {
        this.agentRuntime = agentRuntime;
        this.sessionRepository = sessionRepository;
        this.messageRepository = messageRepository;
        this.metricRepository = metricRepository;
    }

    /**
     * 获取所有 Agent
     */
    @GetMapping
    public Collection<AgentConfig> listAgents() {
        return agentRuntime.getAllAgentConfigs();
    }

    /**
     * 创建 Agent
     */
    @PostMapping
    public AgentConfig createAgent(@RequestBody CreateAgentRequest request) {
        AgentConfig config = AgentConfig.builder()
                .name(request.name())
                .description(request.description())
                .systemPrompt(request.systemPrompt())
                .tools(request.tools())
                .temperature(request.temperature() != null ? request.temperature() : 0.7)
                .maxTokens(request.maxTokens() != null ? request.maxTokens() : 4096)
                .modelName(request.modelName() != null ? request.modelName() : "mimo-v2.5")
                .build();
        agentRuntime.registerAgent(config);
        return config;
    }

    /**
     * 获取 Agent 详情
     */
    @GetMapping("/{name}")
    public AgentConfig getAgent(@PathVariable String name) {
        return agentRuntime.getAgentConfig(name);
    }

    /**
     * 获取对话历史
     */
    @GetMapping("/{name}/history")
    public List<Map<String, Object>> getHistory(
            @PathVariable String name,
            @RequestParam(required = false) String sessionId) {
        if (sessionId != null) {
            return messageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId).stream()
                    .map(this::messageToMap)
                    .toList();
        }
        return messageRepository.findTop50ByAgentNameOrderByCreatedAtDesc(name).stream()
                .map(this::messageToMap)
                .toList();
    }

    /**
     * 获取会话列表
     */
    @GetMapping("/sessions")
    public List<ChatSession> getSessions(@RequestParam(required = false) String agentName) {
        if (agentName != null) {
            return sessionRepository.findByAgentNameOrderByUpdatedAtDesc(agentName);
        }
        return sessionRepository.findAllByOrderByUpdatedAtDesc();
    }

    /**
     * 获取 Agent 记忆
     */
    @GetMapping("/{name}/memory")
    public Map<String, Object> getMemory(
            @PathVariable String name,
            @RequestParam(defaultValue = "20") int count) {
        String memoryContent = agentRuntime.getMemory(name, count);
        return Map.of("agentName", name, "memory", memoryContent, "count", count);
    }

    /**
     * 清空 Agent 记忆
     */
    @DeleteMapping("/{name}/memory")
    public Map<String, String> clearMemory(@PathVariable String name) {
        agentRuntime.clearMemory(name);
        return Map.of("status", "ok", "message", "Memory cleared for agent: " + name);
    }

    /**
     * 获取 Agent 性能统计
     */
    @GetMapping("/metrics/stats")
    public List<Map<String, Object>> getMetricsStats() {
        return metricRepository.getAgentStats().stream()
                .map(row -> Map.of(
                        "agentName", row[0],
                        "totalCalls", row[1],
                        "avgDurationMs", row[2],
                        "successCount", row[3]
                ))
                .toList();
    }

    private Map<String, Object> messageToMap(ChatMessage msg) {
        return Map.of(
                "id", msg.getId(),
                "role", msg.getRole(),
                "content", msg.getContent(),
                "createdAt", msg.getCreatedAt().toString()
        );
    }
}
