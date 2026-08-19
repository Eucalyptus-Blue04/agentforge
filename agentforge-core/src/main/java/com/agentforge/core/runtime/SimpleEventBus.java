package com.agentforge.core.runtime;

import com.agentforge.core.message.AgentEvent;
import com.agentforge.core.message.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * 简单事件总线实现（内存）
 */
public class SimpleEventBus implements EventBus {

    private static final Logger log = LoggerFactory.getLogger(SimpleEventBus.class);

    private final Map<AgentEvent.EventType, List<Consumer<AgentEvent>>> typeHandlers = new ConcurrentHashMap<>();
    private final Map<String, List<Consumer<AgentEvent>>> agentHandlers = new ConcurrentHashMap<>();
    private final List<Consumer<AgentEvent>> globalHandlers = new CopyOnWriteArrayList<>();

    @Override
    public void publish(AgentEvent event) {
        log.debug("Publishing event: type={}, agent={}", event.type(), event.agentName());

        // 全局订阅者
        for (Consumer<AgentEvent> handler : globalHandlers) {
            try {
                handler.accept(event);
            } catch (Exception e) {
                log.error("Error in global event handler", e);
            }
        }

        // 按类型订阅
        List<Consumer<AgentEvent>> byType = typeHandlers.get(event.type());
        if (byType != null) {
            for (Consumer<AgentEvent> handler : byType) {
                try {
                    handler.accept(event);
                } catch (Exception e) {
                    log.error("Error in type event handler for {}", event.type(), e);
                }
            }
        }

        // 按 Agent 订阅
        if (event.agentName() != null) {
            List<Consumer<AgentEvent>> byAgent = agentHandlers.get(event.agentName());
            if (byAgent != null) {
                for (Consumer<AgentEvent> handler : byAgent) {
                    try {
                        handler.accept(event);
                    } catch (Exception e) {
                        log.error("Error in agent event handler for {}", event.agentName(), e);
                    }
                }
            }
        }
    }

    @Override
    public void subscribe(AgentEvent.EventType type, Consumer<AgentEvent> handler) {
        typeHandlers.computeIfAbsent(type, k -> new CopyOnWriteArrayList<>()).add(handler);
    }

    @Override
    public void subscribe(String agentName, Consumer<AgentEvent> handler) {
        agentHandlers.computeIfAbsent(agentName, k -> new CopyOnWriteArrayList<>()).add(handler);
    }

    @Override
    public void subscribeAll(Consumer<AgentEvent> handler) {
        globalHandlers.add(handler);
    }

    @Override
    public void unsubscribe(Consumer<AgentEvent> handler) {
        globalHandlers.remove(handler);
        typeHandlers.values().forEach(list -> list.remove(handler));
        agentHandlers.values().forEach(list -> list.remove(handler));
    }
}
