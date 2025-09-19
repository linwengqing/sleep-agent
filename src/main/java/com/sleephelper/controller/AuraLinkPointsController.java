package com.sleephelper.controller;

import com.sleephelper.bean.BurnResult;
import com.sleephelper.bean.ForwarderParams;
import com.sleephelper.bean.MetaBurnRequest;
import com.sleephelper.common.Result;
import com.sleephelper.service.AuraLinkPointsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/**
 * AuraLink积分控制器
 * 
 * @author SleepHelper
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/aura-link")
@CrossOrigin(origins = "*")
public class AuraLinkPointsController {

    @Autowired
    private AuraLinkPointsService auraLinkPointsService;

    /**
     * 铸造积分
     * 
     * @param toAddress 接收地址
     * @param amount 铸造数量
     * @return 交易哈希
     */
    @PostMapping("/mint")
    public Result<String> mintPoints(@RequestParam String toAddress, 
                                   @RequestParam BigInteger amount) {
        try {
            log.info("收到铸造积分请求: toAddress={}, amount={}", toAddress, amount);
            String transactionHash = auraLinkPointsService.mintPoints(toAddress, amount);
            return Result.success(transactionHash, "铸造积分成功");
        } catch (Exception e) {
            log.error("铸造积分失败", e);
            return Result.error("铸造积分失败: " + e.getMessage());
        }
    }

    /**
     * 查询余额
     * 
     * @param userAddress 用户地址
     * @return 余额
     */
    @GetMapping("/balance")
    public Result<BigInteger> getBalance(@RequestParam String userAddress) {
        try {
            BigInteger balance = auraLinkPointsService.getBalance(userAddress);
            return Result.success("查询余额成功", balance);
        } catch (Exception e) {
            log.error("查询余额失败", e);
            return Result.error("查询余额失败: " + e.getMessage());
        }
    }

    /**
     * 获取代币名称
     * 
     * @return 代币名称
     */
    @GetMapping("/name")
    public Result<String> getName() {
        try {
            String name = auraLinkPointsService.getName();
            return Result.success(name, "获取代币名称成功");
        } catch (Exception e) {
            log.error("获取代币名称失败", e);
            return Result.error("获取代币名称失败: " + e.getMessage());
        }
    }

    /**
     * 获取代币符号
     * 
     * @return 代币符号
     */
    @GetMapping("/symbol")
    public Result<String> getSymbol() {
        try {
            String symbol = auraLinkPointsService.getSymbol();
            return Result.success(symbol, "获取代币符号成功");
        } catch (Exception e) {
            log.error("获取代币符号失败", e);
            return Result.error("获取代币符号失败: " + e.getMessage());
        }
    }

    /**
     * 获取总供应量
     * 
     * @return 总供应量
     */
    @GetMapping("/total-supply")
    public Result<BigInteger> getTotalSupply() {
        try {
            BigInteger totalSupply = auraLinkPointsService.getTotalSupply();
            return Result.success("获取总供应量成功", totalSupply);
        } catch (Exception e) {
            log.error("获取总供应量失败", e);
            return Result.error("获取总供应量失败: " + e.getMessage());
        }
    }

    /**
     * 检查铸造权限
     * 
     * @param account 账户地址
     * @return 是否有铸造权限
     */
    @GetMapping("/has-minter-role")
    public Result<Boolean> hasMinterRole(@RequestParam String account) {
        try {
            boolean hasRole = auraLinkPointsService.hasMinterRole(account);
            return Result.success("检查铸造权限成功", hasRole);
        } catch (Exception e) {
            log.error("检查铸造权限失败", e);
            return Result.error("检查铸造权限失败: " + e.getMessage());
        }
    }

    /**
     * 元交易销毁
     * 
     * @param request 元交易请求
     * @return 销毁结果
     */
    @PostMapping("/burn-meta")
    public Result<BurnResult> relayBurnWithMeta(@RequestBody MetaBurnRequest request) {
        try {
            BurnResult result = auraLinkPointsService.relayBurnWithMeta(request);
            return Result.success("元交易销毁处理完成", result);
        } catch (Exception e) {
            log.error("元交易销毁失败", e);
            return Result.error("元交易销毁失败: " + e.getMessage());
        }
    }

    /**
     * 直接销毁
     * 
     * @param from 被销毁代币的地址
     * @param amount 销毁数量
     * @return 交易哈希
     */
    @PostMapping("/burn-direct")
    public Result<String> burnFromDirect(@RequestParam String from, 
                                       @RequestParam BigInteger amount) {
        try {
            String transactionHash = auraLinkPointsService.burnFromDirect(from, amount);
            return Result.success(transactionHash, "直接销毁成功");
        } catch (Exception e) {
            log.error("直接销毁失败", e);
            return Result.error("直接销毁失败: " + e.getMessage());
        }
    }

    /**
     * 获取销毁Forwarder参数
     * 
     * @param userAddress 用户地址
     * @param amount 销毁数量
     * @return Forwarder参数
     */
    @GetMapping("/burn-forwarder-params")
    public Result<ForwarderParams> getBurnForwarderParams(@RequestParam String userAddress, 
                                                         @RequestParam BigInteger amount) {
        try {
            ForwarderParams params = auraLinkPointsService.getBurnForwarderParams(userAddress, amount);
            return Result.success("获取Forwarder参数成功", params);
        } catch (Exception e) {
            log.error("获取Forwarder参数失败", e);
            return Result.error("获取Forwarder参数失败: " + e.getMessage());
        }
    }

    /**
     * 获取待处理交易数量
     * 
     * @return 待处理交易数量
     */
    @GetMapping("/pending-transaction-count")
    public Result<BigInteger> getPendingTransactionCount() {
        try {
            BigInteger count = auraLinkPointsService.getPendingTransactionCount();
            return Result.success("获取待处理交易数量成功", count);
        } catch (Exception e) {
            log.error("获取待处理交易数量失败", e);
            return Result.error("获取待处理交易数量失败: " + e.getMessage());
        }
    }

    /**
     * 打印账户信息
     * 
     * @return 操作结果
     */
    @PostMapping("/print-account-info")
    public Result<String> printAccountInfo() {
        try {
            auraLinkPointsService.printAccountInfo();
            return Result.success("打印账户信息成功", "操作完成");
        } catch (Exception e) {
            log.error("打印账户信息失败", e);
            return Result.error("打印账户信息失败: " + e.getMessage());
        }
    }
}
