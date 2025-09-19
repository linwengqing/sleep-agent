package com.sleephelper.bean;

import lombok.Data;

import java.math.BigInteger;

/**
 * 元交易销毁请求类
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@Data
public class MetaBurnRequest {
    
    /**
     * 发送方地址
     */
    private String from;
    
    /**
     * 销毁数量
     */
    private BigInteger amount;
    
    /**
     * 随机数
     */
    private BigInteger nonce;
    
    /**
     * Gas 限制
     */
    private BigInteger gas;
    
    /**
     * 签名
     */
    private String signature;
}
