package com.sleephelper.service;

import com.sleephelper.bean.BurnResult;
import com.sleephelper.bean.ForwarderParams;
import com.sleephelper.bean.MetaBurnRequest;

import java.math.BigInteger;

public interface AuraLinkPoints2Service {
	/**
	 * 查询指定地址的代币余额（以基础单位表示，内部已从最小单位换算）。
	 * @param userAddress 用户以太坊地址（0x开头）
	 * @return 余额（基础单位，例如 1 SP）
	 * @throws Exception 查询或链上调用失败
	 */
	//BigInteger getBalance(String userAddress) throws Exception;

	/**
	 * 为指定地址铸造代币。
	 * @param toAddress 接收地址（0x开头）
	 * @param amount 铸造数量（基础单位，将在实现中乘以 10^18）
	 * @return 交易哈希
	 * @throws Exception 无权限、链上回滚或网络异常
	 */
	String mintPoints(String toAddress, BigInteger amount) throws Exception;

	/**
	 * 获取代币名称。
	 * @return 代币名称
	 * @throws Exception 链上调用失败
	 */
	//String getName() throws Exception;

	/**
	 * 获取代币符号。
	 * @return 代币符号
	 * @throws Exception 链上调用失败
	 */
	//String getSymbol() throws Exception;

	/**
	 * 获取代币总供应量（基础单位）。
	 * @return 总供应量
	 * @throws Exception 链上调用失败
	 */
	//BigInteger getTotalSupply() throws Exception;

	/**
	 * 检查账户是否具备铸造权限（包含 MINTER_ROLE 或 DEFAULT_ADMIN_ROLE）。
	 * @param account 账户地址
	 * @return 是否具备铸造权限
	 * @throws Exception 链上调用失败
	 */
	//boolean hasMinterRole(String account) throws Exception;

	/**
	 * 转发客户端已完成 EIP-712 签名的销毁元交易。
	 * @param request 元交易请求（包含 from、amount、nonce、gas、signature）
	 * @return 业务结果（成功标记、交易哈希、消息）
	 */
	//BurnResult relayBurnWithMeta(MetaBurnRequest request);

	/**
	 * 直接调用合约的 burnFrom 进行销毁（非元交易）。
	 * @param from 被销毁代币的地址
	 * @param amount 销毁数量（基础单位，将在实现中乘以 10^18）
	 * @return 交易哈希
	 * @throws Exception 链上回滚或网络异常
	 */
	//String burnFromDirect(String from, BigInteger amount) throws Exception;

	/**
	 * 获取前端发起 EIP-712 签名所需的 Forwarder 参数（域、nonce、gas、functionData 等）。
	 * @param userAddress 用户地址
	 * @param amount 销毁数量（基础单位）
	 * @return Forwarder 参数集
	 * @throws Exception Forwarder 未配置或链上读取失败
	 */
	//ForwarderParams getBurnForwarderParams(String userAddress, BigInteger amount) throws Exception;

	/**
	 * 查询当前服务账户的待处理交易数量（pending - latest）。
	 * @return 待处理交易数量
	 * @throws Exception 节点调用失败
	 */
	//BigInteger getPendingTransactionCount() throws Exception;

	/**
	 * 打印当前服务账户的链上信息（地址、余额、nonce、gas 配置等）。
	 * @throws Exception 节点调用失败
	 */
	//void printAccountInfo() throws Exception;
}
