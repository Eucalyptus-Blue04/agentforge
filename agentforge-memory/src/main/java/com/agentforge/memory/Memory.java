package com.agentforge.memory;

import com.agentforge.core.message.AgentMessage;

import java.util.List;

/**
 * 记忆系统接口
 * <p>
 * 提供 Agent 的短期记忆能力（滑动窗口对话历史）
 */
public interface Memory {

    /**
     * 添加消息到记忆
     */
    void add(AgentMessage message);

    /**
     * 获取最近的 N 条消息
     */
    List<AgentMessage> getRecent(int count);

    /**
     * 获取所有消息
     */
    List<AgentMessage> getAll();

    /**
     * 清空记忆
     */
    void clear();

    /**
     * 获取记忆中的消息数量
     */
    int size();
}
