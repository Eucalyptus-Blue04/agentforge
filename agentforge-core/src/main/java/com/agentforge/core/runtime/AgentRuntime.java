package com.agentforge.core.runtime;

import com.agentforge.core.agent.AgentConfig;
import com.agentforge.core.agent.AgentContext;
import com.agentforge.core.agent.AgentResponse;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Agent 运行时接口
 * <p>
 * 定义 Agent 运行时的核心能力
 */
public interface AgentRuntime {

    /**
     * 注册 Agent 配置
     */
    void registerAgent(AgentConfig config);

    /**
     * 获取 Agent 配置
     */
    AgentConfig getAgentConfig(String agentName);

    /**
     * 执行 Agent 对话（同步）
     *
     * @param agentName Agent 名称
     * @param message   用户消息
     * @param context   执行上下文
     * @return Agent 响应
     */
    AgentResponse execute(String agentName, String message, AgentContext context);

    /**
     * 流式执行 Agent 对话（支持 Function Calling + SSE 推送）
     *
     * @param agentName Agent 名称
     * @param message   用户消息
     * @param context   执行上下文
     * @param onChunk   每个输出块的回调
     */
    void streamExecute(String agentName, String message, AgentContext context,
                       Consumer<StreamChunk> onChunk);

    /**
     * 获取所有注册的 Agent 配置
     */
    Collection<AgentConfig> getAllAgentConfigs();

    /**
     * 获取指定 Agent 的记忆内容
     *
     * @param agentName Agent 名称
     * @param count     获取最近的消息数量
     * @return 记忆内容摘要
     */
    String getMemory(String agentName, int count);

    /**
     * 清空指定 Agent 的记忆
     */
    void clearMemory(String agentName);
}
