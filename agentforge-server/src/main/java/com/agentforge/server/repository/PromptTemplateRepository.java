package com.agentforge.server.repository;

import com.agentforge.server.entity.PromptTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromptTemplateRepository extends JpaRepository<PromptTemplate, Long> {
    List<PromptTemplate> findByCategoryOrderByUseCountDesc(String category);
    List<PromptTemplate> findByIsBuiltinTrueOrderByUseCountDesc();
    List<PromptTemplate> findAllByOrderByUseCountDesc();
}
