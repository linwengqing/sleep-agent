package com.sleephelper.bean;

import lombok.Data;

import java.math.BigInteger;

/**
 * Forwarder 参数类
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@Data
public class ForwarderParams {
    
    /**
     * 域
     */
    private String domain;
    
    /**
     * 随机数
     */
    private BigInteger nonce;
    
    /**
     * Gas 限制
     */
    private BigInteger gasLimit;
    
    /**
     * 函数数据
     */
    private String functionData;
    
    /**
     * 用户地址
     */
    private String userAddress;
    
    /**
     * 销毁数量
     */
    private BigInteger amount;
}
