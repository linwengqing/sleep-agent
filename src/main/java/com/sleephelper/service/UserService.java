package com.sleephelper.service;

import com.sleephelper.entity.User;

/**
 * 用户服务接口
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
public interface UserService {
    
    /**
     * 根据用户ID查询用户信息
     * @param id 用户ID（UUID）
     * @return 用户信息
     */
    User getUserById(String id);
    
    /**
     * 根据钱包地址查询用户信息
     * @param walletAddress 钱包地址
     * @return 用户信息
     */
    User getUserByWalletAddress(String walletAddress);
    
    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);
    
    /**
     * 创建用户
     * @param user 用户信息
     * @return 是否成功
     */
    boolean createUser(User user);
    
    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 是否成功
     */
    boolean updateUser(User user);
}
