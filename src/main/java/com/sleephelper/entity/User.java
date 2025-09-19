package com.sleephelper.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户表实体类
 * 对应数据库表：users
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一标识（UUID）
     */
    private String id;

    /**
     * 用户链上钱包地址（含0x）
     */
    private String walletAddress;

    /**
     * 用户自定义昵称（唯一）
     */
    private String username;

    /**
     * 用户头像URL（存储图片链接）
     */
    private String avatar;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
