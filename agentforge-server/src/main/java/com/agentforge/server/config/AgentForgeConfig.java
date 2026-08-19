package com.agentforge.server.config;

import com.agentforge.core.message.EventBus;
import com.agentforge.core.runtime.AgentRuntime;
import com.agentforge.core.runtime.SimpleEventBus;
import com.agentforge.llm.client.LlmClient;
import com.agentforge.llm.client.OpenAiClient;
import com.agentforge.memory.Memory;
import com.agentforge.memory.shortterm.ShortTermMemory;
import com.agentforge.server.service.DefaultAgentRuntime;
import com.agentforge.tool.builtin.*;
import com.agentforge.tool.registry.ToolRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AgentForge 核心配置
 */
@Configuration
public class AgentForgeConfig {

    private static final Logger log = LoggerFactory.getLogger(AgentForgeConfig.class);

    @Value("${agentforge.llm.api-key:}")
    private String apiKey;

    @Value("${agentforge.llm.base-url:https://api.xiaomi.com/v1}")
    private String baseUrl;

    @Value("${agentforge.llm.model:mimo-v2.5}")
    private String defaultModel;

    @Value("${agentforge.llm.timeout:120}")
    private int timeout;

    @Bean
    public LlmClient llmClient() {
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("未配置 LLM API Key，使用占位 key，实际调用将失败");
            return new OpenAiClient("sk-placeholder", baseUrl, defaultModel, timeout);
        }
        return new OpenAiClient(apiKey, baseUrl, defaultModel, timeout);
    }

    @Bean
    public EventBus eventBus() {
        return new SimpleEventBus();
    }

    @Bean
    public ToolRegistry toolRegistry() {
        ToolRegistry registry = new ToolRegistry();
        // 注册内置工具
        registry.registerAnnotated(new CalculatorTool());
        registry.registerAnnotated(new HttpTool());
        registry.registerAnnotated(new FileTool());
        registry.registerAnnotated(new DateTimeTool());
        registry.registerAnnotated(new JsonTool());
        registry.registerAnnotated(new StringTool());
        registry.registerAnnotated(new SearchTool());
        return registry;
    }

    @Bean
    public Memory shortTermMemory() {
        return new ShortTermMemory(50); // 保留最近50条消息
    }

    @Bean
    public AgentRuntime agentRuntime(LlmClient llmClient, ToolRegistry toolRegistry,
                                     EventBus eventBus, Memory memory) {
        return new DefaultAgentRuntime(llmClient, toolRegistry, eventBus, memory);
    }
}
