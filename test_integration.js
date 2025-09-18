/**
 * 前后端联调测试脚本
 * 测试阿里云百炼大模型集成功能
 */

const axios = require('axios');

// 测试配置
const BASE_URL = 'http://localhost:8080/api';
const TEST_USER_ID = 'user001';
const TEST_DATE = '2024-01-01';

// 创建axios实例
const api = axios.create({
  baseURL: BASE_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
});

/**
 * 测试AI服务健康检查
 */
async function testAIHealth() {
  console.log('\n=== 测试AI服务健康检查 ===');
  try {
    const response = await api.get('/ai/health');
    console.log('✅ AI服务健康检查成功:', response.data);
    return true;
  } catch (error) {
    console.error('❌ AI服务健康检查失败:', error.message);
    return false;
  }
}

/**
 * 测试AI聊天功能
 */
async function testAIChat() {
  console.log('\n=== 测试AI聊天功能 ===');
  try {
    const testMessages = [
      '你好，我是睡眠助手',
      '我最近睡眠质量不好，有什么建议吗？',
      '如何改善深睡质量？',
      '失眠怎么办？'
    ];

    for (const message of testMessages) {
      console.log(`\n发送消息: ${message}`);
      const response = await api.post('/ai/chat', {
        userId: TEST_USER_ID,
        message: message
      });
      
      if (response.data.code === 200) {
        console.log('✅ AI回复:', response.data.data);
      } else {
        console.error('❌ AI聊天失败:', response.data.message);
      }
      
      // 等待1秒避免请求过快
      await new Promise(resolve => setTimeout(resolve, 1000));
    }
    
    return true;
  } catch (error) {
    console.error('❌ AI聊天测试失败:', error.message);
    return false;
  }
}

/**
 * 测试AI睡眠报告生成
 */
async function testAISleepReport() {
  console.log('\n=== 测试AI睡眠报告生成 ===');
  try {
    const response = await api.post('/ai/report', {
      userId: TEST_USER_ID,
      dateOfSleep: TEST_DATE
    });
    
    if (response.data.code === 200) {
      console.log('✅ AI睡眠报告生成成功:');
      console.log(response.data.data);
    } else {
      console.error('❌ AI睡眠报告生成失败:', response.data.message);
    }
    
    return true;
  } catch (error) {
    console.error('❌ AI睡眠报告测试失败:', error.message);
    return false;
  }
}

/**
 * 测试对话历史管理
 */
async function testChatHistory() {
  console.log('\n=== 测试对话历史管理 ===');
  try {
    // 获取对话统计
    const statsResponse = await api.get('/ai/chat/stats', {
      params: { userId: TEST_USER_ID }
    });
    
    if (statsResponse.data.code === 200) {
      console.log('✅ 对话统计:', statsResponse.data.data);
    }
    
    // 清空对话历史
    const clearResponse = await api.post('/ai/chat/clear', {
      userId: TEST_USER_ID
    });
    
    if (clearResponse.data.code === 200) {
      console.log('✅ 清空对话历史成功:', clearResponse.data.data);
    }
    
    return true;
  } catch (error) {
    console.error('❌ 对话历史管理测试失败:', error.message);
    return false;
  }
}

/**
 * 测试帮助信息
 */
async function testHelp() {
  console.log('\n=== 测试帮助信息 ===');
  try {
    const response = await api.get('/ai/help');
    
    if (response.data.code === 200) {
      console.log('✅ 帮助信息获取成功:');
      console.log(response.data.data);
    } else {
      console.error('❌ 帮助信息获取失败:', response.data.message);
    }
    
    return true;
  } catch (error) {
    console.error('❌ 帮助信息测试失败:', error.message);
    return false;
  }
}

/**
 * 测试睡眠数据接口
 */
async function testSleepData() {
  console.log('\n=== 测试睡眠数据接口 ===');
  try {
    // 初始化测试数据
    const initResponse = await api.post('/sleep/data/init');
    if (initResponse.data.code === 200) {
      console.log('✅ 睡眠数据初始化成功');
    }
    
    // 获取睡眠数据
    const dataResponse = await api.get('/sleep/data', {
      params: { 
        userId: TEST_USER_ID, 
        date: TEST_DATE 
      }
    });
    
    if (dataResponse.data.code === 200) {
      console.log('✅ 睡眠数据获取成功:', dataResponse.data.data);
    }
    
    return true;
  } catch (error) {
    console.error('❌ 睡眠数据测试失败:', error.message);
    return false;
  }
}

/**
 * 测试积分接口
 */
async function testPoints() {
  console.log('\n=== 测试积分接口 ===');
  try {
    // 生成积分
    const generateResponse = await api.post('/points/generate', {
      userId: TEST_USER_ID,
      dateOfSleep: TEST_DATE
    });
    
    if (generateResponse.data.code === 200) {
      console.log('✅ 积分生成成功:', generateResponse.data.data);
    }
    
    // 获取当前积分
    const currentResponse = await api.get('/points/current', {
      params: { userId: TEST_USER_ID }
    });
    
    if (currentResponse.data.code === 200) {
      console.log('✅ 当前积分获取成功:', currentResponse.data.data);
    }
    
    return true;
  } catch (error) {
    console.error('❌ 积分测试失败:', error.message);
    return false;
  }
}

/**
 * 主测试函数
 */
async function runIntegrationTests() {
  console.log('🚀 开始前后端联调测试...');
  console.log(`测试环境: ${BASE_URL}`);
  console.log(`测试用户: ${TEST_USER_ID}`);
  console.log(`测试日期: ${TEST_DATE}`);
  
  const tests = [
    { name: 'AI服务健康检查', fn: testAIHealth },
    { name: '睡眠数据接口', fn: testSleepData },
    { name: '积分接口', fn: testPoints },
    { name: 'AI聊天功能', fn: testAIChat },
    { name: 'AI睡眠报告生成', fn: testAISleepReport },
    { name: '对话历史管理', fn: testChatHistory },
    { name: '帮助信息', fn: testHelp },
  ];
  
  let passed = 0;
  let failed = 0;
  
  for (const test of tests) {
    try {
      const result = await test.fn();
      if (result) {
        passed++;
      } else {
        failed++;
      }
    } catch (error) {
      console.error(`❌ ${test.name} 测试异常:`, error.message);
      failed++;
    }
  }
  
  console.log('\n=== 测试结果汇总 ===');
  console.log(`✅ 通过: ${passed}`);
  console.log(`❌ 失败: ${failed}`);
  console.log(`📊 总计: ${passed + failed}`);
  
  if (failed === 0) {
    console.log('🎉 所有测试通过！前后端联调成功！');
  } else {
    console.log('⚠️  部分测试失败，请检查相关功能');
  }
}

// 运行测试
if (require.main === module) {
  runIntegrationTests().catch(console.error);
}

module.exports = {
  runIntegrationTests,
  testAIHealth,
  testAIChat,
  testAISleepReport,
  testChatHistory,
  testHelp,
  testSleepData,
  testPoints
};
