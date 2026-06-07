# 福利院院民档案与日常活动管理系统

## 项目简介

本系统是一个完整的福利院管理系统，包含院民档案管理、日常活动管理、亲属探访预约和突发事件登记等功能。

## 技术栈

- **后端**: Java + Spring Boot 2.7 + MyBatis Plus
- **前端**: Vue 3 + Element Plus + Vite
- **数据库**: MySQL 8.0

## 数据库配置

数据库已创建并导入，配置信息：
- 地址: 127.0.0.1
- 端口: 3306
- 用户名: root
- 密码: 123456
- 数据库名: welfare_home

## 项目结构

```
welfareHomeResident/
├── database/              # 数据库脚本
│   └── welfare_home.sql
├── backend/               # 后端项目
│   ├── src/
│   │   └── main/
│   │       ├── java/com/welfare/
│   │       │   ├── common/          # 通用类
│   │       │   ├── controller/      # 控制器
│   │       │   ├── entity/          # 实体类
│   │       │   ├── mapper/          # 数据访问层
│   │       │   └── service/         # 业务逻辑层
│   │       └── resources/
│   │           └── application.yml  # 配置文件
│   └── pom.xml
└── frontend/              # 前端项目
    ├── src/
    │   ├── views/         # 页面组件
    │   ├── router/        # 路由配置
    │   ├── utils/         # 工具类
    │   └── main.js        # 入口文件
    ├── index.html
    ├── package.json
    └── vite.config.js
```

## 功能模块

### 1. 院民档案管理
- 院民基本信息管理（姓名、性别、身份证号、健康状况等）
- 家庭情况和入院原因记录
- 用药记录管理
- 指定亲属管理

### 2. 日常活动管理
- 每周活动排班表（周一至周日）
- 活动参与人员设置
- 活动签到管理

### 3. 探访预约管理
- 亲属预约探访
- 身份验证（只允许指定亲属探访）
- 预约审核流程

### 4. 突发事件管理
- 突发事件登记（生病、走失、伤亡等）
- 严重等级划分（一般/较重/严重/特别严重）
- 上报级别（院内/区级/市级）
- 事件处理记录

## 启动项目

### 启动后端

```bash
cd backend
mvn spring-boot:run
```

后端服务地址: http://localhost:8080

### 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端服务地址: http://localhost:3000

## 默认账号

- 用户名: admin
- 密码: admin

## 数据库表说明

1. **resident** - 院民档案表
2. **medication_record** - 用药记录表
3. **activity** - 活动表
4. **activity_participant** - 活动参与表
5. **activity_checkin** - 活动签到表
6. **designated_relative** - 指定亲属表
7. **visit_appointment** - 探访预约表
8. **emergency_event** - 突发事件表
9. **sys_user** - 系统用户表
