# AgentForge - AI Agent 协作平台

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen)
![Maven](https://img.shields.io/badge/Maven-3.x-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

**一个功能完整的 AI Agent 协作平台，支持多智能体对话、工具调用、文件分析、语音交互**

</div>

---

## 📖 项目简介

AgentForge 是一个基于 Java 17 + Spring Boot 3.2.5 构建的 AI Agent 协作平台。用户可以创建、配置多个具有不同人格和能力的 AI Agent，通过流式对话与它们交互，并利用内置工具系统扩展 Agent 的能力。

### ✨ 核心特性

- 🤖 **多 Agent 系统** — 创建和管理多个 AI Agent，支持自定义人格、系统提示词、温度、模型选择
- 🔧 **函数调用 (Function Calling)** — 基于注解的工具注册机制，内置 7 类工具
- 💬 **流式对话 (SSE)** — 基于 Server-Sent Events 的实时流式响应
- 📁 **文件分析** — 支持 Word、Excel、PPT、PDF、图片 OCR 等多格式文件解析
- 🎙️ **语音对话** — 基于 Web Speech API 的语音输入/输出
- 🧠 **记忆系统** — 滑动窗口短期记忆，自动管理上下文
- 📡 **WebSocket 事件广播** — 实时推送 Agent 生命周期事件
- 🎨 **现代 UI** — 毛玻璃风格 (Glassmorphism) 响应式界面，支持明暗主题

---

## 🏗️ 项目架构

### 模块结构

```
agentforge/                              # 父 POM
├── agentforge-core/                     # 核心抽象层
│   ├── com.agentforge.core.agent        #   Agent 配置、上下文、响应
│   ├── com.agentforge.core.message      #   消息类型（密封接口）
│   ├── com.agentforge.core.runtime      #   运行时接口、事件总线
│   └── com.agentforge.core.event        #   事件定义
│
├── agentforge-llm/                      # LLM 客户端集成
│   ├── com.agentforge.llm.client        #   OpenAI 兼容 API 客户端
│   └── com.agentforge.llm.function      #   函数调用处理器
│
├── agentforge-tool/                     # 工具系统
│   ├── com.agentforge.tool.annotation   #   @Tool、@ToolParam 注解
│   ├── com.agentforge.tool.registry     #   工具注册中心
│   └── com.agentforge.tool.builtin      #   内置工具（计算器、HTTP、文件等）
│
├── agentforge-memory/                   # 记忆系统
│   └── com.agentforge.memory            #   短期记忆（滑动窗口）
│
└── agentforge-server/                   # Spring Boot 服务端
    ├── config/                          #   配置类
    ├── controller/                      #   REST API 控制器
    ├── service/                         #   核心运行时服务
    ├── entity/                          #   JPA 实体
    ├── repository/                      #   数据仓库
    └── resources/static/                #   前端静态资源
```

### 模块依赖关系

```
                  ┌─────────────┐
                  │   core      │
                  └──────┬──────┘
                 ┌───────┼───────┐
                 ▼       ▼       ▼
           ┌──────┐  ┌──────┐  ┌────────┐
           │ llm  │  │memory│  │  tool  │
           └──┬───┘  └──────┘  └───┬────┘
              │                    │
              │    ┌───────────┐   │
              └───►│  server   │◄──┘
                   └───────────┘
```

---

## 🛠️ 技术栈

| 类别 | 技术 |
|------|------|
| **语言** | Java 17 |
| **框架** | Spring Boot 3.2.5 |
| **构建** | Maven |
| **数据库** | H2 (开发) / MySQL (生产) |
| **ORM** | Spring Data JPA / Hibernate |
| **LLM 客户端** | Java HttpClient (OpenAI 兼容 API) |
| **流式传输** | SSE (Server-Sent Events) |
| **文件解析** | Apache PDFBox 3.0.1, Apache POI 5.2.5 |
| **OCR** | Tess4J 5.11.0 (Tesseract) |
| **序列化** | Jackson 2.15.4 |
| **前端** | 原生 HTML/CSS/JS |
| **语音** | Web Speech API |

---

## 🚀 快速开始

### 环境要求

- **Java 17** 或更高版本
- **Maven 3.6+**
- **Tesseract OCR**（可选，用于图片文字识别）

### 1. 克隆项目

```bash
git clone https://github.com/your-username/agentforge.git
cd agentforge
```

### 2. 配置 LLM API

编辑 `agentforge-server/src/main/resources/application.yml`，配置你的 LLM API 信息：

```yaml
agentforge:
  llm:
    api-key: your-api-key-here
    base-url: https://api.openai.com/v1   # 或其他 OpenAI 兼容 API
    model: gpt-4o-mini
    timeout-seconds: 120
```

> 💡 项目支持所有 OpenAI 兼容的 API，包括 OpenAI、小米 MIMO、SiliconFlow 等。

### 3. 构建项目

```bash
mvn clean package -DskipTests
```

### 4. 启动服务

```bash
java -jar agentforge-server/target/agentforge-server-1.0.0-SNAPSHOT.jar
```

### 5. 访问应用

打开浏览器访问：**http://localhost:8080**

---

## 📋 功能详解

### 🤖 多 Agent 系统

平台内置 11 个预配置的 Demo Agent：

| Agent | 说明 |
|-------|------|
| `researcher` | 研究员 — 擅长信息检索和分析 |
| `analyst` | 分析师 — 数据分析和洞察 |
| `summarizer` | 摘要专家 — 文本总结提炼 |
| `assistant` | 通用助手 — 日常问答 |
| `sarcasm` | 毒舌人格 — 讽刺幽默风格 |
| `gentle` | 温柔人格 — 体贴亲切 |
| `scholar` | 学者人格 — 博学严谨 |
| `comedian` | 搞笑人格 — 幽默风趣 |
| `aloof` | 高冷人格 — 简洁直接 |
| `cute` | 可爱人格 — 活泼俏皮 |
| `verbose` | 啰嗦人格 — 详细冗长 |

可通过 API 或 UI 自定义创建新 Agent：

```json
POST /api/agents
{
  "name": "my-agent",
  "description": "自定义 Agent",
  "systemPrompt": "你是一个专业的...",
  "modelName": "mimo-v2.5",
  "temperature": 0.7,
  "maxTokens": 4096,
  "tools": ["calculator", "http_get", "web_search"]
}
```

### 🔧 内置工具系统

工具通过 `@Tool` 和 `@ToolParam` 注解自动注册：

| 工具 | 功能 | 示例 |
|------|------|------|
| `calculator` | 数学表达式计算 | `(3 + 5) * 2` |
| `http_get` | HTTP GET 请求 | 获取网页/API 数据 |
| `http_post` | HTTP POST 请求 | 提交数据 |
| `file_read` | 读取文件内容 | 读取本地文本文件 |
| `list_directory` | 列出目录文件 | 浏览文件系统 |
| `web_search` | Bing 网络搜索 | 搜索实时信息 |
| `baike_search` | 百度百科搜索 | 查询百科知识 |
| `current_datetime` | 获取当前时间 | 日期时间操作 |
| `json_parse` | JSON 解析/提取 | 处理 JSON 数据 |
| `string_tool` | 字符串操作 | 替换、正则、分割等 |

**自定义工具示例：**

```java
public class MyTool {

    @Tool(name = "my_tool", description = "我的自定义工具")
    public String execute(
            @ToolParam(name = "input", description = "输入参数", required = true) String input) {
        // 工具逻辑
        return "结果: " + input;
    }
}
```

### 💬 流式对话

支持 SSE 流式响应，实时返回 LLM 生成内容：

```bash
curl -X POST http://localhost:8080/api/chat/stream \
  -H "Content-Type: application/json" \
  -d '{
    "message": "你好，请介绍一下自己",
    "agentName": "assistant",
    "sessionId": "session-123"
  }'
```

响应格式 (SSE)：
```
data: {"type":"delta","content":"你好"}
data: {"type":"delta","content":"！我是"}
data: {"type":"delta","content":"AI助手"}
data: {"type":"tool_call","toolName":"web_search","arguments":"..."}
data: {"type":"tool_result","content":"搜索结果..."}
data: {"type":"done","content":""}
```

### 📁 文件上传与分析

支持多种文件格式的内容提取：

```bash
curl -X POST http://localhost:8080/api/chat/upload \
  -F "file=@document.pdf"
```

| 格式 | 支持的扩展名 |
|------|-------------|
| PDF | `.pdf` |
| Word | `.doc`, `.docx` |
| Excel | `.xls`, `.xlsx` |
| PowerPoint | `.ppt`, `.pptx` |
| 图片 (OCR) | `.png`, `.jpg`, `.jpeg`, `.bmp`, `.gif` |
| 文本 | `.txt`, `.md`, `.json`, `.xml`, `.csv`, `.html` |

> 📝 OCR 功能需要安装 Tesseract 并配置 `tessdata/` 目录（已内置中文和英文训练数据）。

### 🎙️ 语音对话

基于浏览器 Web Speech API 的语音交互模式：

1. 点击界面上的🎙️麦克风按钮开启语音模式
2. 说出你的问题（自动语音识别）
3. AI 流式回复并语音播报
4. 支持调节语速（0.8x - 1.5x）

### 🧠 记忆系统

- **短期记忆**：滑动窗口，默认保留最近 50 条消息
- 消息类型追踪：`user`、`assistant`、`tool_call`、`tool_result`
- 每个 Agent 独立的记忆空间
- 支持通过 API 清除记忆

```bash
# 获取记忆
GET /api/agents/{agentName}/memory

# 清除记忆
DELETE /api/agents/{agentName}/memory
```

### 📡 WebSocket 事件

连接 `ws://localhost:8080/ws/agents` 接收实时事件：

```json
{
  "eventType": "AGENT_STARTED",
  "agentName": "assistant",
  "timestamp": "2024-01-01T12:00:00",
  "data": { "sessionId": "session-123" }
}
```

支持的事件类型：
- `AGENT_CREATED` / `AGENT_STARTED` / `AGENT_COMPLETED` / `AGENT_ERROR`
- `MESSAGE_SENT` / `MESSAGE_RECEIVED`
- `TOOL_CALLED` / `TOOL_RESULT`
- `WORKFLOW_STARTED` / `WORKFLOW_STEP` / `WORKFLOW_COMPLETED`

---

## 🔌 API 参考

### 聊天 API

| 方法 | 路径 | 说明 |
|------|------|------|
| `POST` | `/api/chat` | 同步聊天 |
| `POST` | `/api/chat/stream` | SSE 流式聊天 |
| `POST` | `/api/chat/upload` | 文件上传 |
| `POST` | `/api/chat/stream-with-file` | 文件 + 流式聊天 |
| `GET` | `/api/chat/sessions` | 获取会话列表 |
| `GET` | `/api/chat/sessions/{id}/messages` | 获取会话消息 |
| `DELETE` | `/api/chat/sessions/{id}` | 删除会话 |

### Agent API

| 方法 | 路径 | 说明 |
|------|------|------|
| `GET` | `/api/agents` | 获取所有 Agent |
| `GET` | `/api/agents/{name}` | 获取单个 Agent |
| `POST` | `/api/agents` | 创建 Agent |
| `DELETE` | `/api/agents/{name}` | 删除 Agent |
| `GET` | `/api/agents/{name}/memory` | 获取 Agent 记忆 |
| `DELETE` | `/api/agents/{name}/memory` | 清除 Agent 记忆 |
| `GET` | `/api/agents/stats` | 获取 Agent 统计 |

### 模板 API

| 方法 | 路径 | 说明 |
|------|------|------|
| `GET` | `/api/templates` | 获取所有模板 |
| `GET` | `/api/templates/{id}` | 获取单个模板 |
| `POST` | `/api/templates` | 创建模板 |
| `POST` | `/api/templates/{id}/use` | 使用模板（计数+1） |

### 系统 API

| 方法 | 路径 | 说明 |
|------|------|------|
| `GET` | `/health` | 健康检查 |
| `GET` | `/api/info` | 系统信息 |

---

## ⚙️ 配置说明

### application.yml 完整配置

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:h2:mem:agentforge    # H2 内存数据库（开发）
    driver-class-name: org.h2.Driver
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB

agentforge:
  llm:
    api-key: your-api-key
    base-url: https://api.openai.com/v1
    model: gpt-4o-mini
    timeout-seconds: 120

logging:
  level:
    com.agentforge: INFO
```

### 生产环境 MySQL 配置

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/agentforge?useSSL=false&serverTimezone=UTC&characterEncoding=utf8
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: your-password
  jpa:
    database-platform: org.hibernate.dialect.MySQLDialect
```

---

## 📁 项目文件说明

```
agentforge/
├── pom.xml                              # 父 POM
├── README.md                            # 项目文档
├── .gitignore                           # Git 忽略配置
├── start-tunnel.sh                      # localtunnel 内网穿透脚本
├── tessdata/                            # Tesseract OCR 训练数据
│   ├── chi_sim.traineddata              #   中文简体
│   ├── eng.traineddata                  #   英文
│   └── osd.traineddata                  #   方向检测
├── agentforge-core/                     # 核心模块
├── agentforge-llm/                      # LLM 客户端模块
├── agentforge-tool/                     # 工具系统模块
├── agentforge-memory/                   # 记忆系统模块
└── agentforge-server/                   # 服务端模块
    └── src/main/resources/
        ├── application.yml              # 应用配置
        └── static/                      # 前端资源
            ├── index.html               # 主页面
            ├── js/app.js                # 应用逻辑
            └── css/style.css            # 样式文件
```

---

## 🧪 开发指南

### 本地开发

```bash
# 运行测试
mvn test

# 仅构建（跳过测试）
mvn clean package -DskipTests

# 运行开发服务器
cd agentforge-server
mvn spring-boot:run
```

### 添加新的内置工具

1. 在 `agentforge-tool` 模块创建工具类：

```java
package com.agentforge.tool.builtin;

import com.agentforge.tool.annotation.Tool;
import com.agentforge.tool.annotation.ToolParam;

public class NewTool {

    @Tool(name = "new_tool", description = "新工具的描述")
    public String execute(
            @ToolParam(name = "param1", description = "参数说明", required = true) String param1) {
        return "执行结果";
    }
}
```

2. 在 `AgentForgeConfig.java` 中注册：

```java
toolRegistry.registerAnnotated(new NewTool());
```

### 添加新的 LLM 提供商

实现 `LlmClient` 接口或直接使用 `OpenAiClient`（兼容所有 OpenAI API 格式的服务）：

```java
LlmClient client = new OpenAiClient(
    "your-api-key",
    "https://api.example.com/v1",
    "model-name",
    120
);
```

---

## 📊 数据库表结构

| 表名 | 说明 |
|------|------|
| `chat_session` | 聊天会话（ID、Agent、标题、消息数） |
| `chat_message` | 聊天消息（会话ID、角色、内容、工具调用） |
| `prompt_template` | 提示词模板（名称、分类、内容、变量） |
| `agent_metric` | Agent 性能指标（耗时、迭代次数、工具调用数） |

---

## 🌐 内网穿透

使用 localtunnel 将本地服务暴露到公网：

```bash
# 安装 localtunnel
npm install -g localtunnel

# 启动隧道
./start-tunnel.sh
# 或手动执行
lt --port 8080 --subdomain agentforge
```

访问 `https://agentforge.loca.lt` 即可从公网访问。

---

## 📄 许可证

本项目采用 [MIT License](LICENSE) 开源许可证。

---

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'Add amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 创建 Pull Request

---

<div align="center">

**AgentForge** — 让 AI Agent 协作更简单 🚀

</div>