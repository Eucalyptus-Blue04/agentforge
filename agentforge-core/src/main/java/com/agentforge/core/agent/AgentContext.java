package com.agentforge.core.agent;

import com.agentforge.core.message.AgentMessage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Agent 执行上下文 - 携带对话历史、共享变量等
 */
public class AgentContext {

    private final String sessionId;
    private final List<AgentMessage> history;
    private final Map<String, Object> variables;
    private final Map<String, Object> metadata;

    public AgentContext(String sessionId) {
        this.sessionId = sessionId;
        this.history = Collections.synchronizedList(new ArrayList<>());
        this.variables = new ConcurrentHashMap<>();
        this.metadata = new ConcurrentHashMap<>();
    }

    public AgentContext() {
        this(UUID.randomUUID().toString());
    }

    public String getSessionId() {
        return sessionId;
    }

    public List<AgentMessage> getHistory() {
        return Collections.unmodifiableList(history);
    }

    public void addMessage(AgentMessage message) {
        history.add(message);
    }

    public void addMessages(List<AgentMessage> messages) {
        history.addAll(messages);
    }

    @SuppressWarnings("unchecked")
    public <T> T getVariable(String key, Class<T> type) {
        Object value = variables.get(key);
        if (value == null) return null;
        if (!type.isInstance(value)) {
            throw new ClassCastException("Variable '" + key + "' is not of type " + type.getSimpleName());
        }
        return (T) value;
    }

    public void setVariable(String key, Object value) {
        variables.put(key, value);
    }

    public Map<String, Object> getVariables() {
        return Collections.unmodifiableMap(variables);
    }

    @SuppressWarnings("unchecked")
    public <T> T getMetadata(String key, Class<T> type) {
        Object value = metadata.get(key);
        return value != null && type.isInstance(value) ? (T) value : null;
    }

    public void setMetadata(String key, Object value) {
        metadata.put(key, value);
    }

    public AgentContext copy() {
        AgentContext copy = new AgentContext(this.sessionId);
        copy.history.addAll(this.history);
        copy.variables.putAll(this.variables);
        copy.metadata.putAll(this.metadata);
        return copy;
    }
}
