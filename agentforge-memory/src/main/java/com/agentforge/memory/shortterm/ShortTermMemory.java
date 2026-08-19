package com.agentforge.memory.shortterm;

import com.agentforge.core.message.AgentMessage;
import com.agentforge.memory.Memory;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 短期记忆 - 滑动窗口实现
 * <p>
 * 保留最近的 N 条消息，超出窗口的消息会被丢弃
 */
public class ShortTermMemory implements Memory {

    private final int windowSize;
    private final List<AgentMessage> messages;

    public ShortTermMemory(int windowSize) {
        this.windowSize = windowSize;
        this.messages = new CopyOnWriteArrayList<>();
    }

    public ShortTermMemory() {
        this(20); // 默认保留 20 条消息
    }

    @Override
    public void add(AgentMessage message) {
        messages.add(message);
        // 超出窗口大小时移除最早的消息
        while (messages.size() > windowSize) {
            messages.remove(0);
        }
    }

    @Override
    public List<AgentMessage> getRecent(int count) {
        int from = Math.max(0, messages.size() - count);
        return List.copyOf(messages.subList(from, messages.size()));
    }

    @Override
    public List<AgentMessage> getAll() {
        return List.copyOf(messages);
    }

    @Override
    public void clear() {
        messages.clear();
    }

    @Override
    public int size() {
        return messages.size();
    }

    public int getWindowSize() {
        return windowSize;
    }
}
