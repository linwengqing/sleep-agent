package com.sleephelper.controller;

import com.sleephelper.common.Result;
import com.sleephelper.entity.User;
import com.sleephelper.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 根据用户ID查询用户信息
     * GET /api/users/{id}
     * 
     * @param id 用户ID（UUID）
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable("id") String id) {
        try {
            log.info("接收到查询用户信息请求：用户ID={}", id);
            
            if (id == null || id.trim().isEmpty()) {
                return Result.error("用户ID不能为空");
            }
            
            User user = userService.getUserById(id);
            if (user != null) {
                log.info("查询用户信息成功：用户ID={}", id);
                return Result.success("查询用户信息成功", user);
            } else {
                log.info("未找到用户：用户ID={}", id);
                return Result.error("未找到对应的用户");
            }
        } catch (Exception e) {
            log.error("查询用户信息异常：{}", e.getMessage(), e);
            return Result.error("系统异常：" + e.getMessage());
        }
    }

    /**
     * 根据钱包地址查询用户信息
     * GET /api/users/wallet/{walletAddress}
     * 
     * @param walletAddress 钱包地址
     * @return 用户信息
     */
    @GetMapping("/wallet/{walletAddress}")
    public Result<User> getUserByWalletAddress(@PathVariable("walletAddress") String walletAddress) {
        try {
            log.info("接收到根据钱包地址查询用户信息请求：钱包地址={}", walletAddress);
            
            if (walletAddress == null || walletAddress.trim().isEmpty()) {
                return Result.error("钱包地址不能为空");
            }
            
            User user = userService.getUserByWalletAddress(walletAddress);
            if (user != null) {
                log.info("根据钱包地址查询用户信息成功：钱包地址={}", walletAddress);
                return Result.success("查询用户信息成功", user);
            } else {
                log.info("未找到用户：钱包地址={}", walletAddress);
                return Result.error("未找到对应的用户");
            }
        } catch (Exception e) {
            log.error("根据钱包地址查询用户信息异常：{}", e.getMessage(), e);
            return Result.error("系统异常：" + e.getMessage());
        }
    }

    /**
     * 根据用户名查询用户信息
     * GET /api/users/username/{username}
     * 
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping("/username/{username}")
    public Result<User> getUserByUsername(@PathVariable("username") String username) {
        try {
            log.info("接收到根据用户名查询用户信息请求：用户名={}", username);
            
            if (username == null || username.trim().isEmpty()) {
                return Result.error("用户名不能为空");
            }
            
            User user = userService.getUserByUsername(username);
            if (user != null) {
                log.info("根据用户名查询用户信息成功：用户名={}", username);
                return Result.success("查询用户信息成功", user);
            } else {
                log.info("未找到用户：用户名={}", username);
                return Result.error("未找到对应的用户");
            }
        } catch (Exception e) {
            log.error("根据用户名查询用户信息异常：{}", e.getMessage(), e);
            return Result.error("系统异常：" + e.getMessage());
        }
    }

    /**
     * 创建用户
     * POST /api/users
     * 
     * @param user 用户信息
     * @return 创建结果
     */
    @PostMapping
    public Result<User> createUser(@RequestBody User user) {
        try {
            log.info("接收到创建用户请求：钱包地址={}", user.getWalletAddress());
            
            // 参数校验
            if (user.getWalletAddress() == null || user.getWalletAddress().trim().isEmpty()) {
                return Result.error("钱包地址不能为空");
            }
            
            // 检查钱包地址是否已存在
            User existingUser = userService.getUserByWalletAddress(user.getWalletAddress());
            if (existingUser != null) {
                return Result.error("该钱包地址已存在");
            }
            
            // 如果提供了用户名，检查用户名是否已存在
            if (user.getUsername() != null && !user.getUsername().trim().isEmpty()) {
                User existingUserByUsername = userService.getUserByUsername(user.getUsername());
                if (existingUserByUsername != null) {
                    return Result.error("该用户名已存在");
                }
            }
            
            boolean success = userService.createUser(user);
            if (success) {
                log.info("创建用户成功：钱包地址={}", user.getWalletAddress());
                return Result.success("创建用户成功", user);
            } else {
                log.error("创建用户失败：钱包地址={}", user.getWalletAddress());
                return Result.error("创建用户失败");
            }
        } catch (Exception e) {
            log.error("创建用户异常：{}", e.getMessage(), e);
            return Result.error("系统异常：" + e.getMessage());
        }
    }

    /**
     * 更新用户信息
     * PUT /api/users/{id}
     * 
     * @param id 用户ID
     * @param user 用户信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result<User> updateUser(@PathVariable("id") String id, @RequestBody User user) {
        try {
            log.info("接收到更新用户信息请求：用户ID={}", id);
            
            if (id == null || id.trim().isEmpty()) {
                return Result.error("用户ID不能为空");
            }
            
            // 检查用户是否存在
            User existingUser = userService.getUserById(id);
            if (existingUser == null) {
                return Result.error("用户不存在");
            }
            
            // 设置用户ID
            user.setId(id);
            
            // 如果提供了用户名，检查用户名是否已被其他用户使用
            if (user.getUsername() != null && !user.getUsername().trim().isEmpty()) {
                User existingUserByUsername = userService.getUserByUsername(user.getUsername());
                if (existingUserByUsername != null && !existingUserByUsername.getId().equals(id)) {
                    return Result.error("该用户名已被其他用户使用");
                }
            }
            
            boolean success = userService.updateUser(user);
            if (success) {
                log.info("更新用户信息成功：用户ID={}", id);
                return Result.success("更新用户信息成功", user);
            } else {
                log.error("更新用户信息失败：用户ID={}", id);
                return Result.error("更新用户信息失败");
            }
        } catch (Exception e) {
            log.error("更新用户信息异常：{}", e.getMessage(), e);
            return Result.error("系统异常：" + e.getMessage());
        }
    }

    /**
     * 健康检查接口
     * GET /api/users/health
     * 
     * @return 健康状态
     */
    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("用户管理服务运行正常");
    }
}
