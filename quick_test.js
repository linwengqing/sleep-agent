/**
 * 快速测试脚本 - 验证后端服务状态
 */

const axios = require('axios');

async function quickTest() {
  console.log('🔍 快速检查后端服务状态...');
  
  try {
    // 测试后端健康检查
    const response = await axios.get('http://localhost:8080/api/ai/health', {
      timeout: 5000
    });
    
    console.log('✅ 后端服务运行正常');
    console.log('响应数据:', response.data);
    
    // 测试AI聊天
    console.log('\n🤖 测试AI聊天功能...');
    const chatResponse = await axios.post('http://localhost:8080/api/ai/chat', {
      userId: 'test001',
      message: '你好，我是睡眠助手'
    }, {
      timeout: 30000
    });
    
    console.log('✅ AI聊天测试成功');
    console.log('AI回复:', chatResponse.data.data);
    
  } catch (error) {
    if (error.code === 'ECONNREFUSED') {
      console.log('❌ 后端服务未启动，请先启动后端服务');
      console.log('启动命令: mvn spring-boot:run');
    } else if (error.code === 'ECONNABORTED') {
      console.log('⏰ 请求超时，可能是阿里云百炼API响应较慢');
    } else {
      console.log('❌ 测试失败:', error.message);
      if (error.response) {
        console.log('响应状态:', error.response.status);
        console.log('响应数据:', error.response.data);
      }
    }
  }
}

quickTest();
