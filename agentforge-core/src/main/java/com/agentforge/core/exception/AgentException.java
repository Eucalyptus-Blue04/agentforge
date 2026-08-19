package com.agentforge.core.exception;

/**
 * Agent 异常基类
 */
public class AgentException extends RuntimeException {

    private final String agentName;

    public AgentException(String message, String agentName) {
        super(message);
        this.agentName = agentName;
    }

    public AgentException(String message, String agentName, Throwable cause) {
        super(message, cause);
        this.agentName = agentName;
    }

    public String getAgentName() {
        return agentName;
    }
}
