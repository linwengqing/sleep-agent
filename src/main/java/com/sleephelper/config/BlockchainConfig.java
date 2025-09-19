package com.sleephelper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 区块链配置类
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "blockchain")
public class BlockchainConfig {
    
    /**
     * RPC URL
     */
    private String rpcUrl = "https://mainnet.infura.io/v3/YOUR_PROJECT_ID";
    
    /**
     * 代币合约地址
     */
    private String contractAddress = "0x...";
    
    /**
     * 服务账户私钥
     */
    private String privateKey = "YOUR_PRIVATE_KEY";
    
    /**
     * Gas 限制
     */
    private String gasLimit = "21000";
    
    /**
     * Gas 价格
     */
    private String gasPrice = "20000000000"; // 20 Gwei
}
