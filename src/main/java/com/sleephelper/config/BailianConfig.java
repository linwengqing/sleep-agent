package com.sleephelper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云百炼大模型配置类
 * 用于读取application.yml中的百炼配置
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.ai.bailian")
public class BailianConfig {

    /**
     * 阿里云百炼API密钥
     * 建议通过环境变量BAILIAN_API_KEY设置
     */
    private String apiKey;

    /**
     * 阿里云百炼API端点
     */
    private String endpoint = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";

    /**
     * 使用的模型名称
     * 可选值：qwen-turbo, qwen-plus, qwen-max
     */
    private String model = "qwen-plus";

    /**
     * 温度参数，控制生成文本的随机性
     * 范围：0.0-1.0，值越高越随机
     */
    private Double temperature = 0.7;

    /**
     * Top-p参数，核采样参数
     * 范围：0.0-1.0，控制生成文本的多样性
     */
    private Double topP = 0.9;

    /**
     * 最大生成token数
     */
    private Integer maxTokens = 2048;

    /**
     * 请求超时时间（毫秒）
     */
    private Integer timeout = 30000;
}
