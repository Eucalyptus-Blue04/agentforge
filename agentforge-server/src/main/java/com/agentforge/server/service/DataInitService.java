package com.agentforge.server.service;

import com.agentforge.server.entity.PromptTemplate;
import com.agentforge.server.repository.PromptTemplateRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据初始化服务
 */
@Service
public class DataInitService implements CommandLineRunner {

    private final PromptTemplateRepository templateRepository;

    public DataInitService(PromptTemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Override
    public void run(String... args) {
        if (templateRepository.count() == 0) {
            initPromptTemplates();
        }
    }

    private void initPromptTemplates() {
        List<PromptTemplate> templates = List.of(
            createTemplate("代码审查", "编程", "请审查以下代码，指出潜在问题和改进建议：\n\n```\n{{code}}\n```", true),
            createTemplate("代码重构", "编程", "请重构以下代码，提高可读性和性能：\n\n```\n{{code}}\n```", true),
            createTemplate("单元测试", "编程", "请为以下代码编写单元测试：\n\n```\n{{code}}\n```", true),
            createTemplate("API 文档", "编程", "请为以下 API 接口生成文档：\n\n{{api_description}}", true),
            createTemplate("文章摘要", "写作", "请为以下文章生成简洁的摘要：\n\n{{article}}", true),
            createTemplate("文章润色", "写作", "请润色以下文章，提高表达质量：\n\n{{article}}", true),
            createTemplate("翻译", "写作", "请将以下内容翻译为{{target_language}}：\n\n{{content}}", true),
            createTemplate("数据分析", "分析", "请分析以下数据并提供洞察：\n\n{{data}}", true),
            createTemplate("竞品分析", "分析", "请对以下产品进行竞品分析：\n\n产品：{{product}}\n竞品：{{competitors}}", true),
            createTemplate("SWOT 分析", "分析", "请对以下内容进行 SWOT 分析：\n\n{{subject}}", true),
            createTemplate("头脑风暴", "通用", "请围绕以下主题进行头脑风暴，提供至少 10 个创意：\n\n{{topic}}", true),
            createTemplate("问题解决", "通用", "请分析以下问题并提供解决方案：\n\n问题：{{problem}}\n背景：{{context}}", true),
            createTemplate("学习计划", "通用", "请为我制定一个学习{{subject}}的计划，时间{{duration}}，目标：{{goal}}", true),
            createTemplate("面试准备", "通用", "请帮我准备{{position}}职位的面试，包括常见问题和回答要点", true)
        );

        templateRepository.saveAll(templates);
    }

    private PromptTemplate createTemplate(String name, String category, String content, boolean isBuiltin) {
        PromptTemplate template = new PromptTemplate();
        template.setName(name);
        template.setCategory(category);
        template.setContent(content);
        template.setIsBuiltin(isBuiltin);
        template.setDescription("");
        return template;
    }
}
