package com.agentforge.core.agent;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Agent 配置
 */
public record AgentConfig(
    String name,
    String description,
    String systemPrompt,
    List<String> tools,
    double temperature,
    int maxTokens,
    int maxIterations,
    String modelName,
    Map<String, Object> metadata
) {
    public AgentConfig {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Agent name cannot be empty");
        if (systemPrompt == null) systemPrompt = "";
        if (tools == null) tools = Collections.emptyList();
        if (metadata == null) metadata = Collections.emptyMap();
        if (temperature < 0 || temperature > 2) temperature = 0.7;
        if (maxTokens <= 0) maxTokens = 4096;
        if (maxIterations <= 0) maxIterations = 10;
        if (modelName == null || modelName.isBlank()) modelName = "gpt-4o-mini";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String description;
        private String systemPrompt = "";
        private List<String> tools = Collections.emptyList();
        private double temperature = 0.7;
        private int maxTokens = 4096;
        private int maxIterations = 10;
        private String modelName = "gpt-4o-mini";
        private Map<String, Object> metadata = Collections.emptyMap();

        public Builder name(String name) { this.name = name; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder systemPrompt(String systemPrompt) { this.systemPrompt = systemPrompt; return this; }
        public Builder tools(List<String> tools) { this.tools = tools; return this; }
        public Builder temperature(double temperature) { this.temperature = temperature; return this; }
        public Builder maxTokens(int maxTokens) { this.maxTokens = maxTokens; return this; }
        public Builder maxIterations(int maxIterations) { this.maxIterations = maxIterations; return this; }
        public Builder modelName(String modelName) { this.modelName = modelName; return this; }
        public Builder metadata(Map<String, Object> metadata) { this.metadata = metadata; return this; }

        public AgentConfig build() {
            return new AgentConfig(name, description, systemPrompt, tools, temperature, maxTokens, maxIterations, modelName, metadata);
        }
    }
}
