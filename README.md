# Tlias Web Manager

企业级部门与员工管理系统，基于 Spring Boot + MyBatis + Vue 3 构建，提供部门管理、员工管理、考勤打卡、通知公告、看板统计等核心功能，支持基于角色的访问控制（RBAC）。

## 技术栈

| 层级 | 技术 | 版本 |
| --- | --- | --- |
| 语言 | Java | 17 |
| 框架 | Spring Boot | 4.0.6 |
| ORM | MyBatis | 4.0.1 |
| 数据库 | MySQL | 8.x |
| 连接池 | Druid（Alibaba） | 1.2.25 |
| 前端 | Vue 3 + Axios + AdminLTE 3.2 | CDN |
| UI 工具包 | Bootstrap 4.6 + FontAwesome + jQuery | CDN |
| 工具 | Lombok、Commons-IO | - |
| 监控 | Spring Boot Actuator | - |
| 日志 | Logback（滚动文件 + 控制台） | - |

## 功能特性

### 用户管理
- 用户注册与登录（基于 Session 认证）
- 个人信息编辑（用户名、姓名、密码）
- 登录 IP 记录（支持 `X-Forwarded-For` / `X-Real-IP`）

### 角色权限（RBAC）
- **ADMIN（管理员）** — 部门/员工/通知的全部增删改查
- **EMPLOYEE（员工）** — 只读浏览、考勤打卡、标记通知已读

### 部门管理
- 部门列表查询与搜索
- 新增、编辑、删除部门
- 为部门分配/移除员工

### 员工管理
- 员工列表查询与搜索
- 新增、编辑、删除员工
- 自动同步创建系统账号

### 考勤打卡
- 员工可切换在线/离线状态
- 看板实时统计在线人数

### 通知公告
- 管理员发布通知
- 全员查看通知列表
- 逐条或一键全部标记为已读
- 未读红点提醒

### 看板统计
- 部门总数、员工总数、在线人数、管理员数量

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+

### 数据库配置

1. 创建数据库：

```sql
CREATE DATABASE `tlias` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 执行以下建表语句（或参考源码中的 MyBatis Mapper 推断表结构）：

```sql
CREATE TABLE `dept` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `Employee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `dept_id` int DEFAULT NULL,
  `status` varchar(20) DEFAULT 'OFFLINE',
  PRIMARY KEY (`id`)
);

CREATE TABLE `sys_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `role` varchar(20) DEFAULT 'EMPLOYEE',
  PRIMARY KEY (`id`)
);

CREATE TABLE `notification` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(500) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `created_by` int DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `notification_read` (
  `id` int NOT NULL AUTO_INCREMENT,
  `notification_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
);
```

### 修改配置

编辑 `src/main/resources/application.properties`，修改数据库连接信息：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tlias?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false
spring.datasource.username=你的数据库用户名
spring.datasource.password=你的数据库密码
```

### 启动项目

```bash
# 编译打包
mvn clean package

# 启动
java -jar target/tlias-web-manager-0.0.1-SNAPSHOT.jar

# 或使用 Maven 直接启动
mvn spring-boot:run
```

启动后访问：`http://localhost:8080`

## 项目结构

```
src/main/java/com/wb/
├── TliasWebManagerApplication.java    # 应用入口
├── config/
│   ├── LoginInterceptor.java          # 登录拦截 & 权限校验
│   └── WebConfig.java                 # 拦截器注册
├── controller/
│   ├── DashboardController.java       # 看板统计
│   ├── DeptController.java            # 部门 CRUD
│   ├── EmployeeController.java        # 员工 CRUD + 考勤
│   ├── LoginController.java           # 登录/注册/个人信息
│   └── NotificationController.java    # 通知 CRUD + 已读状态
├── entity/
│   ├── Dept.java
│   ├── Employee.java
│   ├── Notification.java
│   ├── Result.java                    # 统一响应体
│   └── SysUser.java
├── mapper/                            # MyBatis 接口
│   ├── DeptMapper.java
│   ├── EmployeeMapper.java
│   ├── NotificationMapper.java
│   └── SysUserMapper.java
└── service/
    ├── DeptService.java
    ├── DeptServiceImpl.java
    ├── EmployeeService.java
    ├── EmployeeServiceImpl.java
    ├── NotificationService.java
    ├── NotificationServiceImpl.java
    ├── SysUserService.java
    └── SysUserServiceImpl.java

src/main/resources/
├── application.properties             # 应用配置
├── logback.xml                        # 日志配置
├── com/wb/mapper/                     # MyBatis XML 映射
│   ├── DeptMapper.xml
│   ├── EmployeeMapper.xml
│   ├── NotificationMapper.xml
│   └── SysUserMapper.xml
└── static/                            # 前端静态页面
    ├── login.html                     # 登录页
    ├── register.html                  # 注册页
    ├── home.html                      # 看板首页
    ├── index.html                     # 部门管理
    ├── employee.html                  # 员工管理
    └── settings.html                  # 系统设置
```

## API 概览

| 方法 | 路径 | 权限 | 说明 |
| --- | --- | --- | --- |
| POST | `/login` | 无需登录 | 用户登录 |
| POST | `/logout` | 已登录 | 退出登录 |
| GET | `/checkLogin` | 已登录 | 获取当前用户信息 |
| POST | `/register` | 无需登录 | 注册新用户 |
| GET/POST/PUT/DELETE | `/depts` | 已登录/管理员 | 部门 CRUD |
| GET/POST/PUT/DELETE | `/employees` | 已登录/管理员 | 员工 CRUD |
| PUT | `/employees/status` | 已登录 | 切换在线/离线 |
| PUT | `/employees/dept` | 管理员 | 分配部门 |
| GET | `/dashboard` | 已登录 | 看板统计数据 |
| GET/POST | `/notifications` | 已登录/管理员 | 通知列表/发布 |
| PUT | `/notifications` | 已登录 | 标记已读 |

## 注意事项

- 当前使用 Session 进行认证，未使用 Token/JWT
- 密码以明文形式存储与传输（**建议生产环境接入 BCrypt 加密**）
- MyBatis Mapper XML 位于 `classpath:com/wb/mapper/` 目录下

## 许可证

[MIT](LICENSE)
