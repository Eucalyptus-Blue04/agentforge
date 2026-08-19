package com.agentforge.tool.registry;

import com.agentforge.llm.function.ToolDefinition;
import com.agentforge.tool.annotation.Tool;
import com.agentforge.tool.annotation.ToolParam;
import com.agentforge.tool.executor.ToolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 工具注册中心
 * <p>
 * 管理所有可用工具，支持注解自动扫描和手动注册
 */
public class ToolRegistry {

    private static final Logger log = LoggerFactory.getLogger(ToolRegistry.class);

    private final Map<String, ToolExecutor> tools = new ConcurrentHashMap<>();

    /**
     * 注册工具实例
     */
    public void register(ToolExecutor executor) {
        String name = executor.getDefinition().name();
        tools.put(name, executor);
        log.info("Registered tool: {}", name);
    }

    /**
     * 通过注解扫描注册工具
     * <p>
     * 扫描对象中所有 @Tool 注解的方法，自动创建 ToolExecutor
     */
    public void registerAnnotated(Object toolInstance) {
        Class<?> clazz = toolInstance.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            Tool toolAnnotation = method.getAnnotation(Tool.class);
            if (toolAnnotation != null) {
                registerMethod(toolInstance, method, toolAnnotation);
            }
        }
    }

    private void registerMethod(Object instance, Method method, Tool annotation) {
        String name = annotation.name();
        String description = annotation.description();

        // 构建参数 Schema
        Map<String, Object> properties = new LinkedHashMap<>();
        List<String> required = new ArrayList<>();

        Parameter[] parameters = method.getParameters();
        for (Parameter param : parameters) {
            ToolParam toolParam = param.getAnnotation(ToolParam.class);
            String paramName = param.getName();
            String paramDesc = toolParam != null ? toolParam.description() : paramName;
            boolean paramRequired = toolParam == null || toolParam.required();

            Map<String, Object> propSchema = new LinkedHashMap<>();
            propSchema.put("description", paramDesc);
            propSchema.put("type", mapJavaType(param.getType()));
            properties.put(paramName, propSchema);

            if (paramRequired) {
                required.add(paramName);
            }
        }

        Map<String, Object> parametersSchema = new LinkedHashMap<>();
        parametersSchema.put("type", "object");
        parametersSchema.put("properties", properties);
        if (!required.isEmpty()) {
            parametersSchema.put("required", required);
        }

        ToolDefinition definition = new ToolDefinition(name, description, parametersSchema);

        // 创建执行器
        ToolExecutor executor = new ToolExecutor() {
            @Override
            public ToolDefinition getDefinition() {
                return definition;
            }

            @Override
            public Object execute(Map<String, Object> params) {
                try {
                    Object[] args = new Object[parameters.length];
                    for (int i = 0; i < parameters.length; i++) {
                        args[i] = params.get(parameters[i].getName());
                    }
                    method.setAccessible(true);
                    return method.invoke(instance, args);
                } catch (Exception e) {
                    throw new RuntimeException("Tool execution failed: " + name, e);
                }
            }
        };

        register(executor);
    }

    private String mapJavaType(Class<?> type) {
        if (type == String.class) return "string";
        if (type == int.class || type == Integer.class || type == long.class || type == Long.class) return "integer";
        if (type == double.class || type == Double.class || type == float.class || type == Float.class) return "number";
        if (type == boolean.class || type == Boolean.class) return "boolean";
        return "string";
    }

    /**
     * 获取工具执行器
     */
    public ToolExecutor getTool(String name) {
        return tools.get(name);
    }

    /**
     * 获取所有工具定义
     */
    public List<ToolDefinition> getAllDefinitions() {
        return tools.values().stream()
                .map(ToolExecutor::getDefinition)
                .toList();
    }

    /**
     * 获取指定名称的工具定义
     */
    public List<ToolDefinition> getDefinitions(List<String> names) {
        if (names == null || names.isEmpty()) {
            return getAllDefinitions();
        }
        return names.stream()
                .map(tools::get)
                .filter(Objects::nonNull)
                .map(ToolExecutor::getDefinition)
                .toList();
    }

    /**
     * 执行工具
     */
    public Object execute(String toolName, Map<String, Object> parameters) {
        ToolExecutor executor = tools.get(toolName);
        if (executor == null) {
            throw new IllegalArgumentException("Tool not found: " + toolName);
        }
        return executor.execute(parameters);
    }

    /**
     * 工具是否存在
     */
    public boolean hasTool(String name) {
        return tools.containsKey(name);
    }

    /**
     * 获取工具数量
     */
    public int size() {
        return tools.size();
    }
}
