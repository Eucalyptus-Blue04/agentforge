package com.agentforge.server.service;

import com.agentforge.core.agent.*;
import com.agentforge.core.message.*;
import com.agentforge.core.exception.AgentException;
import com.agentforge.core.runtime.StreamChunk;
import com.agentforge.llm.client.LlmClient;
import com.agentforge.llm.function.FunctionCallHandler;
import com.agentforge.llm.function.ToolDefinition;
import com.agentforge.llm.model.ChatChunk;
import com.agentforge.llm.model.ChatRequest;
import com.agentforge.llm.model.ChatResponse;
import com.agentforge.memory.Memory;
import com.agentforge.tool.registry.ToolRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Agent 运行时引擎
 * <p>
 * 负责管理 Agent 生命周期、执行对话、处理 Function Calling 循环
 */
public class DefaultAgentRuntime implements com.agentforge.core.runtime.AgentRuntime {

    private static final Logger log = LoggerFactory.getLogger(DefaultAgentRuntime.class);

    private final LlmClient llmClient;
    private final ToolRegistry toolRegistry;
    private final EventBus eventBus;
    private final FunctionCallHandler functionCallHandler;
    private final Memory memory;

    private final Map<String, AgentConfig> agentConfigs = new ConcurrentHashMap<>();

    public DefaultAgentRuntime(LlmClient llmClient, ToolRegistry toolRegistry, EventBus eventBus, Memory memory) {
        this.llmClient = llmClient;
        this.toolRegistry = toolRegistry;
        this.eventBus = eventBus;
        this.functionCallHandler = new FunctionCallHandler();
        this.memory = memory;
    }

    /**
     * 注册 Agent 配置
     */
    @Override
    public void registerAgent(AgentConfig config) {
        agentConfigs.put(config.name(), config);
        eventBus.publish(AgentEvent.of(config.name(), AgentEvent.EventType.AGENT_CREATED));
        log.info("Registered agent: {}", config.name());
    }

    /**
     * 获取 Agent 配置
     */
    @Override
    public AgentConfig getAgentConfig(String agentName) {
        AgentConfig config = agentConfigs.get(agentName);
        if (config == null) {
            throw new AgentException("Agent not found: " + agentName, agentName);
        }
        return config;
    }

    /**
     * 执行 Agent 对话（支持 Function Calling 循环）
     */
    @Override
    public AgentResponse execute(String agentName, String message, AgentContext context) {
        AgentConfig config = getAgentConfig(agentName);
        long startTime = System.currentTimeMillis();

        log.info("Agent [{}] processing message: {}", agentName, truncate(message, 100));
        eventBus.publish(AgentEvent.of(agentName, AgentEvent.EventType.AGENT_STARTED,
                Map.of("message", message)));

        // 将用户消息添加到记忆
        UserMessage userMsg = new UserMessage(agentName, message);
        memory.add(userMsg);
        context.addMessage(userMsg);

        // 构建消息列表
        List<ChatRequest.Message> messages = buildMessages(config, context);
        messages.add(ChatRequest.Message.user(message));

        // 获取工具定义
        List<ToolDefinition> toolDefs = toolRegistry.getDefinitions(config.tools());
        List<ChatRequest.Tool> tools = functionCallHandler.toToolDefinitions(toolDefs);

        // Function Calling 循环
        int iterations = 0;
        String finalContent = null;
        List<AssistantMessage.ToolCall> allToolCalls = new ArrayList<>();

        while (iterations < config.maxIterations()) {
            iterations++;

            // 调用 LLM
            ChatRequest request = new ChatRequest(
                    config.modelName(),
                    messages,
                    config.maxTokens(),
                    config.temperature(),
                    null,
                    false,
                    tools.isEmpty() ? null : tools,
                    null
            );

            ChatResponse response = llmClient.chat(request);
            ChatResponse.Choice choice = response.choices().get(0);
            ChatRequest.Message assistantMsg = choice.message();

            // 添加助手消息到对话
            messages.add(assistantMsg);

            // 检查是否有 tool_calls
            if (assistantMsg.toolCalls() != null && !assistantMsg.toolCalls().isEmpty()) {
                log.debug("Agent [{}] requesting {} tool calls", agentName, assistantMsg.toolCalls().size());

                // 执行每个工具调用
                for (ChatRequest.ToolCall toolCall : assistantMsg.toolCalls()) {
                    String functionName = toolCall.function().name();
                    String callId = toolCall.id();

                    log.info("Agent [{}] calling tool: {}", agentName, functionName);
                    eventBus.publish(AgentEvent.of(agentName, AgentEvent.EventType.TOOL_CALLED,
                            Map.of("tool", functionName)));

                    // 执行工具
                    String result;
                    try {
                        Map<String, Object> params = functionCallHandler.parseArguments(toolCall.function().arguments());
                        Object toolResult = toolRegistry.execute(functionName, params);
                        result = toolResult != null ? toolResult.toString() : "OK";
                    } catch (Exception e) {
                        log.error("Tool execution failed: {}", functionName, e);
                        result = "Error: " + e.getMessage();
                    }

                    // 添加工具结果消息
                    messages.add(ChatRequest.Message.tool(callId, result));

                    // 记录工具调用到记忆
                    ToolCallMessage toolCallMsg = new ToolCallMessage(agentName, callId, functionName, toolCall.function().arguments());
                    ToolResultMessage toolResultMsg = new ToolResultMessage(agentName, callId, functionName, result);
                    memory.add(toolCallMsg);
                    memory.add(toolResultMsg);

                    // 记录工具调用
                    allToolCalls.add(new AssistantMessage.ToolCall(callId, functionName, toolCall.function().arguments()));

                    eventBus.publish(AgentEvent.of(agentName, AgentEvent.EventType.TOOL_RESULT,
                            Map.of("tool", functionName, "result", truncate(result, 200))));
                }

                // 继续循环，让 LLM 处理工具结果
                continue;
            }

            // 没有 tool_calls，结束循环
            finalContent = assistantMsg.content();
            // 如果 content 为空，尝试 reasoning_content（MIMO 思考模型）
            if (finalContent == null || finalContent.isBlank()) {
                finalContent = assistantMsg.reasoningContent();
            }
            break;
        }

        long duration = System.currentTimeMillis() - startTime;
        log.info("Agent [{}] completed in {}ms, {} iterations", agentName, duration, iterations);

        eventBus.publish(AgentEvent.of(agentName, AgentEvent.EventType.AGENT_COMPLETED,
                Map.of("duration", duration, "iterations", iterations)));

        // 将助手回复添加到记忆
        AssistantMessage assistantMessage = new AssistantMessage(agentName, finalContent, allToolCalls);
        memory.add(assistantMessage);
        context.addMessage(assistantMessage);

        return new AgentResponse(finalContent, "stop", allToolCalls, Map.of(), iterations, duration);
    }

    /**
     * 流式执行 Agent 对话（支持 Function Calling + SSE 推送）
     */
    @Override
    public void streamExecute(String agentName, String message, AgentContext context,
                              Consumer<StreamChunk> onChunk) {
        AgentConfig config = getAgentConfig(agentName);
        long startTime = System.currentTimeMillis();

        log.info("Agent [{}] stream processing: {}", agentName, truncate(message, 100));
        eventBus.publish(AgentEvent.of(agentName, AgentEvent.EventType.AGENT_STARTED,
                Map.of("message", message)));

        // 将用户消息添加到记忆
        UserMessage userMsg = new UserMessage(agentName, message);
        memory.add(userMsg);
        context.addMessage(userMsg);

        // 构建消息列表
        List<ChatRequest.Message> messages = buildMessages(config, context);
        messages.add(ChatRequest.Message.user(message));

        // 获取工具定义
        List<ToolDefinition> toolDefs = toolRegistry.getDefinitions(config.tools());
        List<ChatRequest.Tool> tools = functionCallHandler.toToolDefinitions(toolDefs);

        // Function Calling 循环
        int iterations = 0;
        List<AssistantMessage.ToolCall> allToolCalls = new ArrayList<>();

        try {
            while (iterations < config.maxIterations()) {
                iterations++;

                ChatRequest request = new ChatRequest(
                        config.modelName(),
                        messages,
                        config.maxTokens(),
                        config.temperature(),
                        null,
                        true,
                        tools.isEmpty() ? null : tools,
                        null
                );

                // 流式调用 LLM，收集完整响应
                StringBuilder contentBuilder = new StringBuilder();
                List<ChatRequest.ToolCall> responseToolCalls = new ArrayList<>();
                String[] finishReason = {null};

                llmClient.streamChat(request, chunk -> {
                    if (chunk.choices() == null || chunk.choices().isEmpty()) return;
                    ChatChunk.Choice choice = chunk.choices().get(0);
                    if (choice.delta() == null) return;

                    // 推送文本 delta（跳过思考内容）
                    String reasoning = choice.delta().reasoningContent();
                    if (reasoning != null && !reasoning.isEmpty()) return;

                    String content = choice.delta().content();
                    if (content != null && !content.isEmpty()) {
                        contentBuilder.append(content);
                        onChunk.accept(StreamChunk.delta(content));
                    }

                    // 收集 tool_calls
                    if (choice.delta().toolCalls() != null) {
                        for (ChatChunk.ToolCallDelta tc : choice.delta().toolCalls()) {
                            // 累积 tool call（可能分多个 chunk 到达）
                            while (responseToolCalls.size() <= tc.index()) {
                                responseToolCalls.add(new ChatRequest.ToolCall("", "function",
                                        new ChatRequest.FunctionCall("", "")));
                            }
                            ChatRequest.ToolCall existing = responseToolCalls.get(tc.index());
                            String id = tc.id() != null ? tc.id() : existing.id();
                            String name = tc.function() != null && tc.function().name() != null
                                    ? tc.function().name() : existing.function().name();
                            String args = tc.function() != null && tc.function().arguments() != null
                                    ? existing.function().arguments() + tc.function().arguments()
                                    : existing.function().arguments();
                            responseToolCalls.set(tc.index(), new ChatRequest.ToolCall(id, "function",
                                    new ChatRequest.FunctionCall(name, args)));
                        }
                    }

                    if (choice.finishReason() != null) {
                        finishReason[0] = choice.finishReason();
                    }
                });

                // 构建助手消息
                String assistantContent = contentBuilder.toString();
                if (responseToolCalls.isEmpty()) {
                    // 无 tool_calls，结束循环
                    if (assistantContent.isEmpty()) {
                        assistantContent = "（模型未返回有效内容，请重试）";
                    }

                    AssistantMessage assistantMessage = new AssistantMessage(agentName, assistantContent, allToolCalls);
                    memory.add(assistantMessage);
                    context.addMessage(assistantMessage);
                    break;
                }

                // 有 tool_calls，执行工具
                messages.add(ChatRequest.Message.assistant(assistantContent,
                        responseToolCalls.stream()
                                .map(tc -> new ChatRequest.ToolCall(tc.id(), "function",
                                        new ChatRequest.FunctionCall(tc.function().name(), tc.function().arguments())))
                                .collect(Collectors.toList())));

                for (ChatRequest.ToolCall toolCall : responseToolCalls) {
                    String functionName = toolCall.function().name();
                    String callId = toolCall.id();

                    onChunk.accept(StreamChunk.toolCall(callId, functionName));
                    eventBus.publish(AgentEvent.of(agentName, AgentEvent.EventType.TOOL_CALLED,
                            Map.of("tool", functionName)));

                    String result;
                    try {
                        Map<String, Object> params = functionCallHandler.parseArguments(toolCall.function().arguments());
                        Object toolResult = toolRegistry.execute(functionName, params);
                        result = toolResult != null ? toolResult.toString() : "OK";
                    } catch (Exception e) {
                        log.error("Tool execution failed: {}", functionName, e);
                        result = "Error: " + e.getMessage();
                    }

                    messages.add(ChatRequest.Message.tool(callId, result));
                    onChunk.accept(StreamChunk.toolResult(callId, functionName, result));

                    // 记录到记忆
                    ToolCallMessage toolCallMsg = new ToolCallMessage(agentName, callId, functionName, toolCall.function().arguments());
                    ToolResultMessage toolResultMsg = new ToolResultMessage(agentName, callId, functionName, result);
                    memory.add(toolCallMsg);
                    memory.add(toolResultMsg);

                    allToolCalls.add(new AssistantMessage.ToolCall(callId, functionName, toolCall.function().arguments()));

                    eventBus.publish(AgentEvent.of(agentName, AgentEvent.EventType.TOOL_RESULT,
                            Map.of("tool", functionName, "result", truncate(result, 200))));
                }
            }

            long duration = System.currentTimeMillis() - startTime;
            log.info("Agent [{}] stream completed in {}ms, {} iterations", agentName, duration, iterations);
            eventBus.publish(AgentEvent.of(agentName, AgentEvent.EventType.AGENT_COMPLETED,
                    Map.of("duration", duration, "iterations", iterations)));

            onChunk.accept(StreamChunk.done());

        } catch (Exception e) {
            log.error("Agent [{}] stream error: {}", agentName, e.getMessage(), e);
            onChunk.accept(StreamChunk.error(e.getMessage() != null ? e.getMessage() : "调用失败"));
        }
    }

    /**
     * 获取指定 Agent 的记忆内容
     */
    @Override
    public String getMemory(String agentName, int count) {
        List<AgentMessage> recentMessages = memory.getRecent(count);
        StringBuilder sb = new StringBuilder();
        for (AgentMessage msg : recentMessages) {
            if (msg.agentName().equals(agentName)) {
                sb.append(formatMessage(msg)).append("\n");
            }
        }
        return sb.length() > 0 ? sb.toString() : "暂无记忆";
    }

    /**
     * 清空记忆
     */
    @Override
    public void clearMemory(String agentName) {
        memory.clear();
        log.info("Memory cleared for agent: {}", agentName);
    }

    /**
     * 构建消息列表（System Prompt + 历史）
     */
    private List<ChatRequest.Message> buildMessages(AgentConfig config, AgentContext context) {
        List<ChatRequest.Message> messages = new ArrayList<>();

        // System Prompt
        if (config.systemPrompt() != null && !config.systemPrompt().isBlank()) {
            messages.add(ChatRequest.Message.system(config.systemPrompt()));
        }

        // 从记忆中获取相关历史
        List<AgentMessage> memoryMessages = memory.getRecent(20);
        for (AgentMessage msg : memoryMessages) {
            if (msg.agentName().equals(config.name())) {
                addMessageToChat(messages, msg);
            }
        }

        return messages;
    }

    /**
     * 将 Agent 消息转换为 ChatRequest 消息
     */
    private void addMessageToChat(List<ChatRequest.Message> messages, AgentMessage msg) {
        if (msg instanceof UserMessage user) {
            messages.add(ChatRequest.Message.user(user.content()));
        } else if (msg instanceof AssistantMessage assistant) {
            if (assistant.toolCalls() != null && !assistant.toolCalls().isEmpty()) {
                List<ChatRequest.ToolCall> toolCalls = assistant.toolCalls().stream()
                        .map(tc -> new ChatRequest.ToolCall(tc.id(), "function",
                                new ChatRequest.FunctionCall(tc.functionName(), tc.arguments())))
                        .collect(Collectors.toList());
                messages.add(ChatRequest.Message.assistant(assistant.content(), toolCalls));
            } else {
                messages.add(ChatRequest.Message.assistant(assistant.content()));
            }
        } else if (msg instanceof SystemMessage system) {
            messages.add(ChatRequest.Message.system(system.content()));
        } else if (msg instanceof ToolResultMessage toolResult) {
            messages.add(ChatRequest.Message.tool(toolResult.toolCallId(), toolResult.result()));
        }
    }

    /**
     * 格式化消息用于显示
     */
    private String formatMessage(AgentMessage msg) {
        if (msg instanceof UserMessage user) {
            return "[用户] " + user.content();
        } else if (msg instanceof AssistantMessage assistant) {
            return "[助手] " + assistant.content();
        } else if (msg instanceof ToolCallMessage toolCall) {
            return "[工具调用] " + toolCall.toolName() + "(" + toolCall.arguments() + ")";
        } else if (msg instanceof ToolResultMessage toolResult) {
            return "[工具结果] " + toolResult.toolName() + ": " + truncate(toolResult.result(), 100);
        }
        return "[" + msg.type() + "] " + msg.id();
    }

    private String truncate(String s, int maxLen) {
        if (s == null) return "null";
        return s.length() > maxLen ? s.substring(0, maxLen) + "..." : s;
    }

    /**
     * 获取所有注册的 Agent 配置
     */
    @Override
    public Collection<AgentConfig> getAllAgentConfigs() {
        return agentConfigs.values();
    }
}
