package com.sleephelper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * AI相关配置类
 * 配置阿里云百炼大模型相关的Bean
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@Configuration
public class AIConfig {

    /**
     * 配置WebClient
     * 用于调用阿里云百炼API
     * 
     * @return WebClient HTTP客户端
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)); // 10MB
    }
}
