package com.agentforge.core.exception;

/**
 * LLM 客户端异常
 */
public class LlmClientException extends RuntimeException {

    private final int statusCode;

    public LlmClientException(String message) {
        super(message);
        this.statusCode = -1;
    }

    public LlmClientException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = -1;
    }

    public LlmClientException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
