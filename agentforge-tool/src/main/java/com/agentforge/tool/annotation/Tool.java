package com.agentforge.tool.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 工具方法注解
 * <p>
 * 标记在方法上，表示该方法可以被 Agent 通过 Function Calling 调用
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Tool {
    /**
     * 工具名称（用于 Function Calling）
     */
    String name();

    /**
     * 工具描述（LLM 用来理解何时调用此工具）
     */
    String description();
}
