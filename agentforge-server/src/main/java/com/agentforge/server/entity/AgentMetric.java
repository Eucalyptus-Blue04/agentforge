package com.agentforge.server.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Agent 性能指标实体
 */
@Entity
@Table(name = "agent_metrics")
public class AgentMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String agentName;

    @Column(nullable = false)
    private LocalDateTime recordedAt;

    private Long durationMs;

    private Integer iterations;

    private Integer toolCallCount;

    @Column(length = 20)
    private String status; // success, error

    @Column(length = 500)
    private String errorMessage;

    @PrePersist
    protected void onCreate() {
        recordedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }

    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }

    public Long getDurationMs() { return durationMs; }
    public void setDurationMs(Long durationMs) { this.durationMs = durationMs; }

    public Integer getIterations() { return iterations; }
    public void setIterations(Integer iterations) { this.iterations = iterations; }

    public Integer getToolCallCount() { return toolCallCount; }
    public void setToolCallCount(Integer toolCallCount) { this.toolCallCount = toolCallCount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
