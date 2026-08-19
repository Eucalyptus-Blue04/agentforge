package com.agentforge.server.controller;

import com.agentforge.server.entity.PromptTemplate;
import com.agentforge.server.repository.PromptTemplateRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 提示词模板 API
 */
@RestController
@RequestMapping("/api/templates")
public class PromptTemplateController {

    private final PromptTemplateRepository templateRepository;

    public PromptTemplateController(PromptTemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    /**
     * 获取所有模板
     */
    @GetMapping
    public List<PromptTemplate> listTemplates(@RequestParam(required = false) String category) {
        if (category != null) {
            return templateRepository.findByCategoryOrderByUseCountDesc(category);
        }
        return templateRepository.findAllByOrderByUseCountDesc();
    }

    /**
     * 获取模板详情
     */
    @GetMapping("/{id}")
    public PromptTemplate getTemplate(@PathVariable Long id) {
        return templateRepository.findById(id).orElseThrow();
    }

    /**
     * 创建模板
     */
    @PostMapping
    public PromptTemplate createTemplate(@RequestBody PromptTemplate template) {
        template.setIsBuiltin(false);
        template.setUseCount(0);
        return templateRepository.save(template);
    }

    /**
     * 使用模板（增加使用次数）
     */
    @PostMapping("/{id}/use")
    public Map<String, Object> useTemplate(@PathVariable Long id) {
        PromptTemplate template = templateRepository.findById(id).orElseThrow();
        template.setUseCount(template.getUseCount() + 1);
        templateRepository.save(template);
        return Map.of("content", template.getContent(), "name", template.getName());
    }

    /**
     * 删除模板
     */
    @DeleteMapping("/{id}")
    public Map<String, String> deleteTemplate(@PathVariable Long id) {
        templateRepository.deleteById(id);
        return Map.of("status", "ok");
    }
}
