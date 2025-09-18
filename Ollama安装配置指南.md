# Ollama 安装配置指南

## 概述

Ollama 是一个本地运行大型语言模型的工具，支持多种开源模型。本指南将帮助您在本地安装和配置 Ollama，以支持睡眠助手的 AI 功能。

## 系统要求

- **操作系统**: Windows 10/11, macOS 10.15+, Linux
- **内存**: 至少 8GB RAM（推荐 16GB+）
- **存储**: 至少 10GB 可用空间
- **网络**: 首次下载模型需要网络连接

## 安装步骤

### Windows 安装

1. **下载安装包**
   ```bash
   # 访问官网下载
   https://ollama.ai/download
   
   # 或使用 PowerShell 下载
   Invoke-WebRequest -Uri "https://ollama.ai/download/windows" -OutFile "ollama-windows-amd64.exe"
   ```

2. **运行安装程序**
   - 双击下载的安装包
   - 按照安装向导完成安装
   - 安装完成后会自动启动 Ollama 服务

3. **验证安装**
   ```bash
   # 打开命令提示符或 PowerShell
   ollama --version
   ```

### macOS 安装

1. **使用 Homebrew 安装**
   ```bash
   brew install ollama
   ```

2. **手动安装**
   ```bash
   # 下载安装包
   curl -fsSL https://ollama.ai/install.sh | sh
   ```

3. **启动服务**
   ```bash
   ollama serve
   ```

### Linux 安装

1. **使用安装脚本**
   ```bash
   curl -fsSL https://ollama.ai/install.sh | sh
   ```

2. **手动安装**
   ```bash
   # Ubuntu/Debian
   sudo apt update
   sudo apt install ollama
   
   # CentOS/RHEL
   sudo yum install ollama
   ```

3. **启动服务**
   ```bash
   # 作为系统服务启动
   sudo systemctl start ollama
   sudo systemctl enable ollama
   
   # 或手动启动
   ollama serve
   ```

## 模型配置

### 下载 Llama3 模型

```bash
# 下载 Llama3 模型（约 4.7GB）
ollama pull llama3

# 验证模型下载
ollama list
```

### 其他推荐模型

```bash
# 下载其他可用模型
ollama pull llama3.1:8b        # Llama3.1 8B 版本
ollama pull mistral:7b         # Mistral 7B
ollama pull codellama:7b       # Code Llama 7B
ollama pull gemma:7b           # Google Gemma 7B
```

## 配置验证

### 1. 检查服务状态

```bash
# 检查 Ollama 服务是否运行
curl http://localhost:11434/api/tags

# 预期返回类似：
{
  "models": [
    {
      "name": "llama3:latest",
      "model": "llama3:latest",
      "size": 4720000000,
      "digest": "sha256:...",
      "details": {
        "parent_model": "",
        "format": "gguf",
        "family": "llama",
        "families": ["llama"],
        "parameter_size": "8B",
        "quantization_level": "Q4_0"
      }
    }
  ]
}
```

### 2. 测试模型响应

```bash
# 测试 Llama3 模型
ollama run llama3 "你好，请介绍一下自己"

# 预期返回：
# 你好！我是 Llama3，一个由 Meta 开发的大型语言模型...
```

### 3. 检查 API 接口

```bash
# 测试 API 接口
curl -X POST http://localhost:11434/api/generate \
  -H "Content-Type: application/json" \
  -d '{
    "model": "llama3",
    "prompt": "你好",
    "stream": false
  }'
```

## Spring Boot 集成配置

### 1. 应用配置

确保 `application.yml` 中的配置正确：

```yaml
spring:
  ai:
    ollama:
      base-url: http://localhost:11434
      chat:
        model: llama3
        options:
          temperature: 0.7
          top-p: 0.9
          max-tokens: 2048
```

### 2. 测试连接

启动 Spring Boot 应用后，访问健康检查接口：

```bash
curl http://localhost:8080/api/ai/health
```

### 3. 测试 AI 功能

```bash
# 测试睡眠报告生成
curl -X POST http://localhost:8080/api/ai/report \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user001",
    "dateOfSleep": "2024-01-01"
  }'

# 测试 AI 聊天
curl -X POST http://localhost:8080/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user001",
    "message": "我最近总是失眠，怎么办？"
  }'
```

## 性能优化

### 1. 模型选择

- **Llama3 8B**: 平衡性能和资源消耗，推荐用于生产环境
- **Llama3.1 8B**: 改进版本，更好的中文支持
- **Mistral 7B**: 更小的模型，适合资源受限环境

### 2. 系统优化

```bash
# 设置环境变量优化性能
export OLLAMA_NUM_PARALLEL=4
export OLLAMA_MAX_LOADED_MODELS=2
export OLLAMA_FLASH_ATTENTION=1
```

### 3. 内存管理

```bash
# 查看模型内存使用
ollama ps

# 卸载不使用的模型
ollama rm <model-name>
```

## 常见问题

### 1. 模型下载失败

```bash
# 检查网络连接
ping ollama.ai

# 手动下载模型
ollama pull llama3 --verbose
```

### 2. 服务启动失败

```bash
# 检查端口占用
netstat -tulpn | grep 11434

# 查看日志
ollama serve --verbose
```

### 3. 内存不足

```bash
# 使用量化模型
ollama pull llama3:7b-q4_0

# 或使用更小的模型
ollama pull mistral:7b
```

### 4. 响应速度慢

```bash
# 调整并发数
export OLLAMA_NUM_PARALLEL=2

# 使用 GPU 加速（如果支持）
export OLLAMA_GPU_LAYERS=32
```

## 安全配置

### 1. 网络访问控制

```bash
# 限制访问来源
export OLLAMA_HOST=127.0.0.1:11434

# 或使用防火墙
sudo ufw allow from 192.168.1.0/24 to any port 11434
```

### 2. 模型安全

```bash
# 验证模型完整性
ollama list --digest

# 使用官方模型
ollama pull llama3:official
```

## 监控和维护

### 1. 日志监控

```bash
# 查看服务日志
journalctl -u ollama -f

# 或查看应用日志
tail -f /var/log/ollama.log
```

### 2. 性能监控

```bash
# 监控资源使用
htop
nvidia-smi  # 如果使用 GPU

# 监控 API 调用
curl http://localhost:11434/api/version
```

### 3. 定期维护

```bash
# 更新模型
ollama pull llama3:latest

# 清理缓存
ollama rm unused-model

# 重启服务
sudo systemctl restart ollama
```

## 故障排除

### 1. 连接问题

```bash
# 检查服务状态
systemctl status ollama

# 重启服务
sudo systemctl restart ollama

# 检查端口
ss -tulpn | grep 11434
```

### 2. 模型问题

```bash
# 重新下载模型
ollama rm llama3
ollama pull llama3

# 检查模型完整性
ollama list
```

### 3. 性能问题

```bash
# 检查系统资源
free -h
df -h

# 调整配置
export OLLAMA_NUM_PARALLEL=1
export OLLAMA_MAX_LOADED_MODELS=1
```

## 总结

通过以上配置，您应该能够成功运行 Ollama 并集成到睡眠助手系统中。如果遇到问题，请检查：

1. Ollama 服务是否正常运行
2. 模型是否正确下载
3. 网络连接是否正常
4. 系统资源是否充足
5. 配置文件是否正确

更多详细信息请参考 [Ollama 官方文档](https://ollama.ai/docs)。
