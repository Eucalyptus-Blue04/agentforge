package com.agentforge.server.websocket;

import com.agentforge.core.message.AgentEvent;
import com.agentforge.core.message.EventBus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Agent WebSocket 处理器
 * <p>
 * 实时推送 Agent 事件到前端
 */
@Component
public class AgentWebSocketHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(AgentWebSocketHandler.class);

    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AgentWebSocketHandler(EventBus eventBus) {
        // 订阅所有 Agent 事件并广播到 WebSocket 客户端
        eventBus.subscribeAll(event -> {
            broadcastEvent(event);
        });
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        log.info("WebSocket connected: {}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        log.info("WebSocket disconnected: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.debug("Received WebSocket message: {}", message.getPayload());
        // 客户端消息目前不做处理，仅用于保持连接
    }

    private void broadcastEvent(AgentEvent event) {
        if (sessions.isEmpty()) return;

        try {
            Map<String, Object> payload = Map.of(
                    "type", event.type().toString(),
                    "agentName", event.agentName() != null ? event.agentName() : "",
                    "timestamp", event.timestamp().toString(),
                    "data", event.data() != null ? event.data() : Map.of()
            );
            String json = objectMapper.writeValueAsString(payload);
            TextMessage textMessage = new TextMessage(json);

            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    try {
                        session.sendMessage(textMessage);
                    } catch (Exception e) {
                        log.error("Failed to send WebSocket message", e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to serialize event", e);
        }
    }
}
