package com.agentforge.server.repository;

import com.agentforge.server.entity.AgentMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentMetricRepository extends JpaRepository<AgentMetric, Long> {
    List<AgentMetric> findTop100ByAgentNameOrderByRecordedAtDesc(String agentName);

    @Query("SELECT m.agentName, COUNT(m), AVG(m.durationMs), SUM(CASE WHEN m.status = 'success' THEN 1 ELSE 0 END) FROM AgentMetric m GROUP BY m.agentName")
    List<Object[]> getAgentStats();
}
