package com.agentforge.server.repository;

import com.agentforge.server.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySessionIdOrderByCreatedAtAsc(String sessionId);
    List<ChatMessage> findTop50ByAgentNameOrderByCreatedAtDesc(String agentName);
    long countBySessionId(String sessionId);
}
