package com.sleephelper.mapper;

import com.sleephelper.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户表 Mapper 接口
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@Mapper
public interface UserMapper {

    /**
     * 根据用户ID查询用户信息
     * @param id 用户ID（UUID）
     * @return 用户信息
     */
    User selectById(@Param("id") String id);

    /**
     * 根据钱包地址查询用户信息
     * @param walletAddress 钱包地址
     * @return 用户信息
     */
    User selectByWalletAddress(@Param("walletAddress") String walletAddress);

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 用户信息
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 插入用户信息
     * @param user 用户信息
     * @return 影响行数
     */
    int insert(User user);

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 影响行数
     */
    int updateById(User user);

    /**
     * 根据用户ID删除用户
     * @param id 用户ID
     * @return 影响行数
     */
    int deleteById(@Param("id") String id);
}
