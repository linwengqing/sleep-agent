package com.sleephelper.bean;

import lombok.Data;

/**
 * 销毁结果类
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@Data
public class BurnResult {
    
    /**
     * 是否成功
     */
    private boolean success;
    
    /**
     * 交易哈希
     */
    private String transactionHash;
    
    /**
     * 消息
     */
    private String message;
    
    public BurnResult() {}
    
    public BurnResult(boolean success, String transactionHash, String message) {
        this.success = success;
        this.transactionHash = transactionHash;
        this.message = message;
    }
    
    public static BurnResult success(String transactionHash) {
        return new BurnResult(true, transactionHash, "操作成功");
    }
    
    public static BurnResult failure(String message) {
        return new BurnResult(false, null, message);
    }
}
