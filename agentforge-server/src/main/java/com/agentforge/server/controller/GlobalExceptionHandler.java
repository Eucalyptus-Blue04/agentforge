package com.agentforge.server.controller;

import com.agentforge.core.exception.AgentException;
import com.agentforge.core.exception.LlmClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;

/**
 * 全局异常处理器 - 确保所有 API 错误返回标准 JSON 格式
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoResourceFoundException.class)
    public void handleNoResource(NoResourceFoundException e) {
        // favicon.ico 等静态资源缺失不报错
    }

    @ExceptionHandler(AgentException.class)
    public ResponseEntity<Map<String, Object>> handleAgentException(AgentException e) {
        log.error("Agent error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "error", "agent_error",
                "message", e.getMessage() != null ? e.getMessage() : "Agent 执行错误"
        ));
    }

    @ExceptionHandler(LlmClientException.class)
    public ResponseEntity<Map<String, Object>> handleLlmClientException(LlmClientException e) {
        log.error("LLM client error: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of(
                "error", "llm_error",
                "message", e.getMessage() != null ? e.getMessage() : "LLM 服务调用失败"
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception e) {
        // 静态资源相关错误不报错
        if (e.getMessage() != null && e.getMessage().contains("static resource")) {
            return null;
        }
        log.error("Unexpected error: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "internal_error",
                "message", e.getMessage() != null ? e.getMessage() : "服务器内部错误"
        ));
    }
}
