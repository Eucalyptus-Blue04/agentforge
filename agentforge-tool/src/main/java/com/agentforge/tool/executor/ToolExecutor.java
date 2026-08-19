package com.agentforge.tool.executor;

import com.agentforge.llm.function.ToolDefinition;

import java.util.Map;

/**
 * 工具执行器接口
 * <p>
 * 每个工具实现此接口，提供定义和执行逻辑
 */
public interface ToolExecutor {

    /**
     * 获取工具定义（用于 Function Calling）
     */
    ToolDefinition getDefinition();

    /**
     * 执行工具
     *
     * @param parameters 参数
     * @return 执行结果
     */
    Object execute(Map<String, Object> parameters);
}
