package com.agentforge.tool.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 工具参数注解
 * <p>
 * 标记在方法参数上，描述参数的用途
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ToolParam {
    /**
     * 参数描述
     */
    String description();

    /**
     * 是否必填
     */
    boolean required() default true;
}
