package com.agentforge.core.message;

import java.util.function.Consumer;

/**
 * 事件总线接口 - Agent 间通信的核心基础设施
 */
public interface EventBus {

    /**
     * 发布事件
     */
    void publish(AgentEvent event);

    /**
     * 订阅指定类型的事件
     */
    void subscribe(AgentEvent.EventType type, Consumer<AgentEvent> handler);

    /**
     * 订阅指定 Agent 的事件
     */
    void subscribe(String agentName, Consumer<AgentEvent> handler);

    /**
     * 订阅所有事件
     */
    void subscribeAll(Consumer<AgentEvent> handler);

    /**
     * 取消订阅
     */
    void unsubscribe(Consumer<AgentEvent> handler);
}
