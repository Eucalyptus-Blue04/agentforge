package com.agentforge.server.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 提示词模板实体
 */
@Entity
@Table(name = "prompt_templates")
public class PromptTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, length = 50)
    private String category; // 通用, 编程, 写作, 分析, 翻译

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(length = 500)
    private String variables; // JSON 格式的变量列表

    @Column(nullable = false)
    private Boolean isBuiltin = false;

    @Column(nullable = false)
    private Integer useCount = 0;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getVariables() { return variables; }
    public void setVariables(String variables) { this.variables = variables; }

    public Boolean getIsBuiltin() { return isBuiltin; }
    public void setIsBuiltin(Boolean isBuiltin) { this.isBuiltin = isBuiltin; }

    public Integer getUseCount() { return useCount; }
    public void setUseCount(Integer useCount) { this.useCount = useCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
