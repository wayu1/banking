# 银行交易管理系统 (Banking Transaction Service)

![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.0.2-green?logo=springboot)  
![Java](https://img.shields.io/badge/Java-17-blue?logo=java)  
![H2 Database](https://img.shields.io/badge/H2_Database-2.2.220-green?logo=h2)

---

## 📌 项目概述

本项目是一个基于 **Spring Boot 3.0.2** 的轻量级银行交易管理系统，支持以下核心功能：

- ✅ 创建交易（金额、类型、描述等）
- ✅ 修改交易
- ✅ 删除交易
- ✅ 分页查询交易列表
- ✅ 错误处理与验证
- ✅ 单元测试 & 压力测试
- ✅ Docker 容器化部署

---

## 🛠️ 技术栈

| 模块 | 技术/工具             | 说明 |
|------|-------------------|------|
| **后端框架** | Spring Boot 3.0.2 | 快速构建 RESTful API |
| **数据库** | H2 内存数据库          | 无需外部依赖，适合测试 |
| **构建工具** | Maven 3.8.6       | 项目管理与依赖 |
| **测试框架** | JUnit 5 + Mockito | 单元测试 |
| **压力测试** | ContiPerf 1.1.0   | 高并发测试 |
| **容器化** | Docker            | 快速部署 |

---

## 🚀 快速开始

### 1. 克隆项目

```bash
git clone https://github.com/wayu1/banking.git
cd banking
```
### 2. 构建应用JAR包
```bash
mvn clean package
mvn install
```
构建成功后，JAR 文件将位于：
target/bank-0.0.1-SNAPSHOT.jar
### 3.构建 Docker 镜像
在项目根目录执行：
```bash
chmod +x start.sh
./start.sh
```
### 4.服务启动后，可通过以下地址访问：
http://localhost:8080

## 测试
### 单元测试
在test目录下的BankingApplicationTests类中
执行单元测试

### 接口测试
#### 1.创建交易
URL:http://localhost:8080/bank/transaction/createTransaction

Request:POST

RequestBody
```json
{
  "userUid": "uid1",
  "referenceId": "mvp-001",
  "amount": 1,
  "type": "DEPOSIT",
  "description": ""
}
```

#### 2.修改交易
URL:http://localhost:8080/bank/transaction/updateTransaction

Request:POST

RequestBody
```json
{
  "userUid": "ccc",
  "referenceId": "ccc",
  "amount": 1,
  "description": ""
}
```

#### 3.删除交易
URL:http://localhost:8080/bank/transaction/deleteTransaction

Request:POST

RequestBody
```json
{
  "userUid": "cccc",
  "referenceId": "xxxxx"
}
```

#### 3.删除交易
URL:http://localhost:8080/bank/transaction/pageTransaction

Request:GET

Query
pageNo:1
pageSize:10
userUid:uid1

