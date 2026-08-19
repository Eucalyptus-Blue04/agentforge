package com.agentforge.server.repository;

import com.agentforge.server.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    List<ChatSession> findByAgentNameOrderByUpdatedAtDesc(String agentName);
    Optional<ChatSession> findBySessionId(String sessionId);
    List<ChatSession> findAllByOrderByUpdatedAtDesc();
}
