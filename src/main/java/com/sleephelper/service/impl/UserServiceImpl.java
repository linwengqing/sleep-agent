package com.sleephelper.service.impl;

import com.sleephelper.entity.User;
import com.sleephelper.mapper.UserMapper;
import com.sleephelper.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 用户服务实现类
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserById(String id) {
        try {
            return userMapper.selectById(id);
        } catch (Exception e) {
            log.error("根据用户ID查询用户失败: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public User getUserByWalletAddress(String walletAddress) {
        try {
            return userMapper.selectByWalletAddress(walletAddress);
        } catch (Exception e) {
            log.error("根据钱包地址查询用户失败: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public User getUserByUsername(String username) {
        try {
            return userMapper.selectByUsername(username);
        } catch (Exception e) {
            log.error("根据用户名查询用户失败: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean createUser(User user) {
        try {
            // 生成UUID作为用户ID
            if (user.getId() == null || user.getId().trim().isEmpty()) {
                user.setId(UUID.randomUUID().toString());
            }
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            int result = userMapper.insert(user);
            return result > 0;
        } catch (Exception e) {
            log.error("创建用户失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean updateUser(User user) {
        try {
            user.setUpdatedAt(LocalDateTime.now());
            int result = userMapper.updateById(user);
            return result > 0;
        } catch (Exception e) {
            log.error("更新用户失败: {}", e.getMessage(), e);
            return false;
        }
    }
}
