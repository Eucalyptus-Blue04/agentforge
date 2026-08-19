package com.agentforge.llm.client;

import com.agentforge.core.exception.LlmClientException;
import com.agentforge.llm.model.ChatChunk;
import com.agentforge.llm.model.ChatRequest;
import com.agentforge.llm.model.ChatResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.function.Consumer;

/**
 * OpenAI 兼容 API 客户端
 * <p>
 * 支持 OpenAI、小米 MIMO、硅基流动等兼容接口
 */
public class OpenAiClient implements LlmClient {

    private static final Logger log = LoggerFactory.getLogger(OpenAiClient.class);
    private static final String CHAT_ENDPOINT = "/chat/completions";

    private final String apiKey;
    private final String baseUrl;
    private final String defaultModel;
    private final int timeoutSeconds;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OpenAiClient(String apiKey) {
        this(apiKey, "https://api.openai.com/v1", "gpt-4o-mini", 120);
    }

    public OpenAiClient(String apiKey, String baseUrl) {
        this(apiKey, baseUrl, "gpt-4o-mini", 120);
    }

    public OpenAiClient(String apiKey, String baseUrl, String defaultModel, int timeoutSeconds) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl != null ? baseUrl : "https://api.openai.com/v1";
        this.defaultModel = defaultModel != null ? defaultModel : "gpt-4o-mini";
        this.timeoutSeconds = timeoutSeconds > 0 ? timeoutSeconds : 120;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public ChatResponse chat(ChatRequest request) {
        // 如果请求中没有指定 model，使用默认模型
        ChatRequest finalRequest = request;
        if (request.model() == null || request.model().isBlank()) {
            finalRequest = new ChatRequest(
                    defaultModel, request.messages(), request.maxTokens(),
                    request.temperature(), request.topP(), request.stream(),
                    request.tools(), request.toolChoice()
            );
        }

        String requestBody = serialize(finalRequest);
        log.debug("Sending chat request to {}: {}", baseUrl + CHAT_ENDPOINT, truncate(requestBody, 500));

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + CHAT_ENDPOINT))
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, java.nio.charset.StandardCharsets.UTF_8))
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            log.debug("Received response: status={}, body={}", response.statusCode(), truncate(response.body(), 500));

            if (response.statusCode() != 200) {
                throw new LlmClientException(
                        "API error (status " + response.statusCode() + "): " + response.body(),
                        response.statusCode());
            }

            return objectMapper.readValue(response.body(), ChatResponse.class);
        } catch (LlmClientException e) {
            throw e;
        } catch (Exception e) {
            throw new LlmClientException("Failed to call API: " + e.getMessage(), e);
        }
    }

    @Override
    public void streamChat(ChatRequest request, Consumer<ChatChunk> onChunk) {
        ChatRequest streamRequest = new ChatRequest(
                request.model() != null ? request.model() : defaultModel,
                request.messages(), request.maxTokens(),
                request.temperature(), request.topP(), true,
                request.tools(), request.toolChoice()
        );

        String requestBody = serialize(streamRequest);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + CHAT_ENDPOINT))
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Authorization", "Bearer " + apiKey)
                .header("Accept", "text/event-stream")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, java.nio.charset.StandardCharsets.UTF_8))
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .build();

        try {
            HttpResponse<java.util.stream.Stream<String>> response = httpClient.send(
                    httpRequest, HttpResponse.BodyHandlers.ofLines());

            if (response.statusCode() != 200) {
                String errorBody = response.body().reduce("", (a, b) -> a + b);
                throw new LlmClientException(
                        "API stream error (status " + response.statusCode() + "): " + errorBody,
                        response.statusCode());
            }

            response.body().forEach(line -> {
                if (line.startsWith("data: ")) {
                    String data = line.substring(6).trim();
                    if ("[DONE]".equals(data)) return;
                    try {
                        ChatChunk chunk = objectMapper.readValue(data, ChatChunk.class);
                        onChunk.accept(chunk);
                    } catch (JsonProcessingException e) {
                        log.warn("Failed to parse SSE chunk: {}", data, e);
                    }
                }
            });
        } catch (LlmClientException e) {
            throw e;
        } catch (Exception e) {
            throw new LlmClientException("Failed to stream API: " + e.getMessage(), e);
        }
    }

    private String serialize(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new LlmClientException("Failed to serialize request: " + e.getMessage(), e);
        }
    }

    private String truncate(String s, int maxLen) {
        if (s == null) return "null";
        return s.length() > maxLen ? s.substring(0, maxLen) + "..." : s;
    }
}
