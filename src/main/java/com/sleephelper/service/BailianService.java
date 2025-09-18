package com.sleephelper.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleephelper.config.BailianConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 阿里云百炼大模型服务类
 * 负责调用阿里云百炼API进行文本生成
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@Slf4j
@Service
public class BailianService {

    @Autowired
    private BailianConfig bailianConfig;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 调用阿里云百炼API生成文本
     * 
     * @param prompt 输入提示词
     * @return 生成的文本内容
     */
    public String generateText(String prompt) {
        try {
            log.info("开始调用阿里云百炼API，模型：{}，提示词长度：{}", bailianConfig.getModel(), prompt.length());

            // 构建请求体
            Map<String, Object> requestBody = buildRequestBody(prompt);

            // 创建WebClient
            WebClient webClient = webClientBuilder
                    .baseUrl(bailianConfig.getEndpoint())
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + bailianConfig.getApiKey())
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            // 发送请求
            String response = webClient.post()
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofMillis(bailianConfig.getTimeout()))
                    .block();

            // 解析响应
            String generatedText = parseResponse(response);
            
            log.info("阿里云百炼API调用成功，生成文本长度：{}", generatedText.length());
            return generatedText;

        } catch (WebClientResponseException e) {
            log.error("阿里云百炼API调用失败，HTTP状态码：{}，响应体：{}", e.getStatusCode(), e.getResponseBodyAsString());
            return handleApiError(e);
        } catch (Exception e) {
            log.error("阿里云百炼API调用异常：{}", e.getMessage(), e);
            return "抱歉，AI服务暂时不可用，请稍后重试。";
        }
    }

    /**
     * 构建请求体
     * 
     * @param prompt 输入提示词
     * @return 请求体Map
     */
    private Map<String, Object> buildRequestBody(String prompt) {
        Map<String, Object> requestBody = new HashMap<>();
        
        // 模型参数
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("temperature", bailianConfig.getTemperature());
        parameters.put("top_p", bailianConfig.getTopP());
        parameters.put("max_tokens", bailianConfig.getMaxTokens());
        
        // 消息内容
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);
        
        requestBody.put("model", bailianConfig.getModel());
        requestBody.put("parameters", parameters);
        requestBody.put("input", Map.of("messages", List.of(message)));
        
        return requestBody;
    }

    /**
     * 解析API响应
     * 
     * @param response API响应字符串
     * @return 生成的文本内容
     */
    private String parseResponse(String response) {
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            
            // 检查是否有错误
            if (rootNode.has("error")) {
                String errorMessage = rootNode.get("error").get("message").asText();
                log.error("阿里云百炼API返回错误：{}", errorMessage);
                return "抱歉，AI服务返回错误：" + errorMessage;
            }
            
            // 提取生成的文本
            JsonNode outputNode = rootNode.path("output");
            if (outputNode.has("text")) {
                return outputNode.get("text").asText();
            }
            
            // 尝试从choices中提取
            JsonNode choicesNode = rootNode.path("output").path("choices");
            if (choicesNode.isArray() && choicesNode.size() > 0) {
                JsonNode firstChoice = choicesNode.get(0);
                if (firstChoice.has("message") && firstChoice.get("message").has("content")) {
                    return firstChoice.get("message").get("content").asText();
                }
            }
            
            log.warn("无法从API响应中提取文本内容，原始响应：{}", response);
            return "抱歉，无法解析AI响应内容。";
            
        } catch (Exception e) {
            log.error("解析阿里云百炼API响应失败：{}", e.getMessage(), e);
            return "抱歉，AI响应解析失败。";
        }
    }

    /**
     * 处理API错误
     * 
     * @param e WebClient响应异常
     * @return 友好的错误提示
     */
    private String handleApiError(WebClientResponseException e) {
        int statusCode = e.getStatusCode().value();
        String responseBody = e.getResponseBodyAsString();
        
        switch (statusCode) {
            case 401:
                return "API密钥无效，请检查配置。";
            case 403:
                return "API访问被拒绝，请检查权限设置。";
            case 429:
                return "API调用频率超限，请稍后重试。";
            case 500:
                return "阿里云服务暂时不可用，请稍后重试。";
            default:
                log.error("阿里云百炼API调用失败，状态码：{}，响应：{}", statusCode, responseBody);
                return "AI服务暂时不可用，请稍后重试。";
        }
    }
}
