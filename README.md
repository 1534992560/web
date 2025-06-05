# 家庭财务管理系统

## 项目简介
家庭财务管理系统是一个基于Spring Boot开发的Web应用程序，旨在帮助家庭更好地管理日常收支、预算和财务统计。系统提供了完整的用户管理、账单管理、统计分析等功能，支持多人协作使用。

## 主要功能
- 📝 用户管理
  - 邮箱验证码注册
  - 管理员权限管理
  - 家庭成员管理
  - 密码重置
- 💰 账单管理
  - 收入支出记录
  - 消费类型管理
  - 收入类型管理
  - 账单详情查看
- 📊 统计分析
  - 收支统计
  - 消费类型分析
  - 收入类型分析
  - 账单趋势分析
- 🏠 家庭管理
  - 家庭成员添加/删除
  - 权限分配

## 技术栈
### 后端
- Spring Boot
- MyBatis
- MySQL
- Spring Security
- Quartz
- JavaMail

### 前端
- Vue.js
- Element UI
- Axios
- ECharts

## 系统要求
- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+
- Node.js 12+

## 快速开始

### 1. 克隆项目
```bash
git clone [项目地址]
cd 家庭财务管理系统
```

### 2. 配置数据库
- 创建MySQL数据库
- 修改`application.yml`中的数据库配置

### 3. 配置邮箱服务
在`application.yml`中配置邮箱服务器信息：
```yaml
spring:
  mail:
    host: smtp.example.com
    port: 587
    username: your-email@example.com
    password: your-password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

### 4. 启动后端服务
```bash
cd AccountApi
mvn spring-boot:run
```

### 5. 启动前端服务
```bash
cd frontend
npm install
npm run serve
```


## 项目结构
```
AccountApi/
├── src/main/java/com/example/account/
│   ├── controller/     # 控制器
│   ├── service/        # 服务层
│   ├── entity/         # 实体类
│   ├── mapper/         # 数据库操作
│   ├── dto/           # 数据传输对象
│   ├── param/         # 请求参数对象
│   └── util/          # 工具类
└── src/main/resources/
    └── application.yml # 配置文件

accountVue/
├── src/
    ├── components/    # Vue组件
    ├── assets/        # 静态资源
    ├── util/       # 请求发送
    ├── main.js     # 路由配置
    ├── App.vue      # 入口组件
```

## 特色功能
1. 邮箱验证码注册
   - 安全的用户注册流程
   - 验证码有效期管理
   - 邮箱唯一性验证

2. 权限管理
   - 细粒度的权限控制
   - 管理员权限转让
   - 角色基础的访问控制

3. 数据统计
   - 多维度数据分析
   - 可视化图表展示
   - 自定义统计周期
