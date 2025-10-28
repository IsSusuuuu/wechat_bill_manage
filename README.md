# 微信财务监控系统

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen)
![License](https://img.shields.io/badge/License-MIT-blue)
![H2 Database](https://img.shields.io/badge/Database-H2-blue)

一个现代化的微信账单管理与财务分析系统，支持账单导入、智能分类、规则配置和可视化统计分析。

[功能特性](#-功能特性) • [快速开始](#-快速开始) • [使用指南](#-使用指南) • [开发文档](#️-开发文档)

</div>

---

## ✨ 功能特性

### 📊 账单管理
- 📥 **Excel导入**: 支持微信账单Excel文件一键导入
- 🔍 **智能查询**: 支持按日期、类型、分类、关键词等多维度筛选
- 📄 **分页展示**: 高效的数据分页显示，流畅的用户体验
- 📋 **批次管理**: 记录每次导入的详细信息，支持批次查询和追溯
- 🔄 **自动去重**: 基于哈希算法避免重复导入相同账单

### 🏷️ 分类管理
- 🌲 **两级分类**: 支持一级和二级分类的层级结构
- ✏️ **灵活编辑**: 可新增、修改、删除分类项
- 🎯 **分类类型**: 区分收入和支出分类
- 🔗 **关联查询**: 查看每个分类下的账单数量和金额

### 🤖 智能规则
- 🎲 **关键词匹配**: 基于商品名称、交易对方等字段自动分类
- 🎚️ **优先级控制**: 设置规则优先级，支持多规则匹配
- ⚡ **实时生效**: 规则配置后立即对新导入账单生效
- 📝 **规则管理**: 支持规则的增删改查和启用/禁用

### 📈 数据分析
- 📊 **月度统计**: 可视化展示全年每月收支趋势折线图
- 📉 **季度统计**: 按季度汇总收支情况，把握财务走向
- 🥧 **分类占比**: 饼图展示各分类支出占比，了解消费结构
- 🎯 **自定义时段**: 支持自定义日期范围的数据统计
- 💰 **首页看板**: 今日和本月收支汇总一目了然

### 🎨 用户体验
- 🍎 **Apple风格UI**: 采用现代化的Apple设计风格
- 📱 **响应式设计**: 完美适配桌面端和移动端
- ⚡ **快速响应**: 优化的前后端交互，毫秒级响应
- 🎯 **直观操作**: 简洁明了的界面，无需培训即可上手

---

## 🚀 快速开始

### 系统要求

- **操作系统**: Windows / macOS / Linux
- **内存**: 建议 512MB 以上
- **数据库**: 内置H2数据库，零配置
- **Java**: 
  - 独立版：**无需安装**（推荐给普通用户）
  - JAR版：需要 JDK 17+
  - 源码版：需要 JDK 17+ 和 Maven

---

### 方式一：绿色免安装版 ⭐**无需安装Java**（推荐）

> 适合不想安装Java的普通用户，解压即用！

1. **下载绿色版**
   ```
   从Releases下载：WeChatFinance-Portable.zip
   ```

2. **解压并运行**
   - Windows: 双击 `启动.bat`
   - Linux/Mac: 运行 `./启动.sh`

3. **访问系统**
   
   浏览器打开：http://localhost:8080/index.html

**优点：**
- ✅ 无需安装Java
- ✅ 解压即用，绿色便携
- ✅ 数据独立，易于备份

---

### 方式二：运行JAR包（需要Java）

> 适合已安装Java的用户

1. **下载JAR包**
   ```bash
   # 从Releases下载最新版本
   MoneyManageSystem-0.0.1-SNAPSHOT.jar
   ```

2. **运行应用**
   ```bash
   java -jar MoneyManageSystem-0.0.1-SNAPSHOT.jar
   ```

3. **访问系统**
   
   浏览器打开：http://localhost:8080/index.html

---

### 方式三：运行EXE文件（仅Windows，需要Java）

1. **下载EXE文件**
   ```
   微信财务监控系统.exe
   ```

2. **双击运行**
   
   直接双击exe文件即可启动（需要配置好JAVA_HOME环境变量）

3. **访问系统**
   
   浏览器打开：http://localhost:8080/index.html

---

### 方式四：从源码运行（开发者）

```bash
# 1. 克隆项目
git clone https://github.com/your-username/MoneyManageSystem.git

# 2. 进入项目目录
cd MoneyManageSystem

# 3. 运行项目
mvn spring-boot:run

# 4. 访问系统
浏览器打开 http://localhost:8080/index.html
```

---

## 📖 使用指南

### 1️⃣ 导出微信账单

1. 打开微信 → **我** → **服务** → **钱包**
2. 点击右上角 **账单** → 右上角 **···** → **账单下载**
3. 选择时间范围，下载账单Excel文件

### 2️⃣ 配置分类体系

1. 进入 **收支分类** 页面
2. 点击 **新增一级分类**，创建主分类（如：餐饮、交通、娱乐等）
3. 选择一级分类，点击 **新增二级分类**，创建子分类（如：早餐、午餐、晚餐等）
4. 根据个人需求调整分类结构

### 3️⃣ 设置自动分类规则

1. 进入 **自动填充规则** 页面
2. 点击 **新增规则**
3. 填写规则信息：
   - **匹配字段**: 选择要匹配的字段（商品、交易对方）
   - **关键词**: 输入关键词（如：美团、滴滴）
   - **分类**: 选择目标分类
   - **优先级**: 设置优先级（数字越大优先级越高）
4. 保存规则

### 4️⃣ 导入账单

1. 进入 **账单导入** 页面
2. 点击 **选择文件**，选择微信账单Excel文件
3. 点击 **上传**，系统自动解析并导入
4. 查看导入结果：
   - 成功导入的记录数
   - 自动分类的记录数
   - 跳过的重复记录数

### 5️⃣ 查看统计报表

1. 进入 **首页** 查看今日和本月汇总
2. 进入 **分析报表** 页面
3. 查看各类统计图表：
   - **月度收支趋势**: 选择年份，查看全年每月收支走势
   - **季度收支趋势**: 选择年份，查看季度收支对比
   - **支出分类占比**: 选择时间范围，查看各分类支出占比

### 6️⃣ 管理导入批次

1. 进入 **导入批次** 页面
2. 查看所有导入记录：
   - 导入时间
   - 文件名
   - 导入数量
   - 状态信息
3. 点击批次可查看该批次导入的详细账单

---

## 🏗️ 项目架构

### 技术栈

**后端:**
- Spring Boot 3.5.6
- MyBatis 3.0.5
- H2 Database 2.x
- Apache POI 5.2.5 (Excel解析)
- PageHelper 6.0.0 (分页)

**前端:**
- 原生 HTML5 + CSS3 + JavaScript
- Chart.js 4.4.0 (图表)
- Apple风格UI设计

**构建工具:**
- Maven 3.6+
- Launch4j (EXE打包)

### 项目结构

```
MoneyManageSystem/
├── src/
│   ├── main/
│   │   ├── java/com/susu/
│   │   │   ├── controller/          # REST API控制器
│   │   │   │   ├── BillController.java           # 账单管理
│   │   │   │   ├── CategoryController.java       # 分类管理
│   │   │   │   ├── CategoryRuleController.java   # 规则管理
│   │   │   │   ├── ImportController.java         # 账单导入
│   │   │   │   ├── ImportSessionController.java  # 批次管理
│   │   │   │   └── ReportController.java         # 统计报表
│   │   │   ├── service/             # 业务逻辑层
│   │   │   │   └── impl/            # 服务实现
│   │   │   ├── mapper/              # MyBatis数据访问层
│   │   │   ├── domain/              # 领域模型
│   │   │   │   ├── entity/          # 实体类
│   │   │   │   ├── dto/             # 数据传输对象
│   │   │   │   └── vo/              # 视图对象
│   │   │   ├── config/              # 配置类
│   │   │   ├── utils/               # 工具类
│   │   │   └── Result/              # 统一响应结果
│   │   └── resources/
│   │       ├── mapper/              # MyBatis XML映射文件
│   │       ├── static/              # 前端静态资源
│   │       │   ├── index.html       # 首页
│   │       │   ├── categories.html  # 分类管理页
│   │       │   ├── rules.html       # 规则管理页
│   │       │   ├── import.html      # 导入页面
│   │       │   ├── import-sessions.html  # 批次管理页
│   │       │   ├── reports.html     # 报表页面
│   │       │   ├── styles.css       # 样式文件
│   │       │   └── utils.js         # 工具函数
│   │       ├── schema.sql           # 数据库表结构
│   │       ├── data.sql             # 初始数据
│   │       └── application.properties  # 应用配置
│   └── test/                        # 测试代码
├── data/                            # H2数据库文件目录（运行时创建）
├── pom.xml                          # Maven配置
├── exe.xml                          # Launch4j配置
└── README.md                        # 项目说明
```

### 数据库设计

#### 核心表

1. **wechat_bill** - 微信账单表
   - 存储所有导入的账单记录
   - 包含交易时间、金额、类型、分类等信息
   - 使用bill_hash字段实现去重

2. **bill_category** - 分类表
   - 支持两级分类结构
   - 区分收入和支出分类
   - parent_id字段建立父子关系

3. **category_rule** - 分类规则表
   - 存储自动分类规则
   - 支持关键词匹配
   - 优先级控制

4. **import_session** - 导入批次表
   - 记录每次导入的元数据
   - 关联导入的账单记录
   - 便于追溯和管理

---

## 🛠️ 开发文档

### 环境准备

1. **安装JDK 17+**
   ```bash
   # 验证安装
   java -version
   ```

2. **安装Maven 3.6+**
   ```bash
   # 验证安装
   mvn -version
   ```

3. **IDE推荐**
   - IntelliJ IDEA (推荐)
   - Eclipse
   - VS Code

### 本地开发

```bash
# 1. 克隆项目
git clone https://github.com/your-username/MoneyManageSystem.git
cd MoneyManageSystem

# 2. 编译项目
mvn clean compile

# 3. 运行项目
mvn spring-boot:run

# 4. 访问应用
http://localhost:8080/index.html

# 5. 访问API文档
http://localhost:8080/swagger-ui.html
```

### 打包部署

#### 打包成JAR

```bash
# 跳过测试快速打包
mvn clean package -DskipTests

# 生成的JAR文件位置
target/MoneyManageSystem-0.0.1-SNAPSHOT.jar

# 运行JAR
java -jar target/MoneyManageSystem-0.0.1-SNAPSHOT.jar
```

#### 打包成EXE（Windows）

1. **安装Launch4j**
   
   下载并安装：https://launch4j.sourceforge.net/

2. **打包JAR文件**
   ```bash
   mvn clean package -DskipTests
   ```

3. **使用Launch4j打包**
   - 打开Launch4j
   - 加载项目根目录的 `exe.xml` 配置文件
   - 点击"Build wrapper"生成EXE文件

#### 打包成独立运行版（无需Java）⭐推荐

**Windows 快速打包：**

```bash
# 一键打包（自动化脚本）
build-standalone.bat
```

**手动打包步骤：**

1. **打包JAR**
   ```bash
   mvn clean package -DskipTests
   ```

2. **下载JRE 17**
   
   从 [Adoptium](https://adoptium.net/temurin/releases/?version=17) 下载JRE

3. **创建发布目录**
   ```
   WeChatFinance-Portable/
   ├── jre/              # 解压JRE到这里
   ├── app/              # 复制JAR文件到这里
   ├── 启动.bat          # 启动脚本
   └── README.txt       # 使用说明
   ```

4. **压缩打包**
   
   将整个目录压缩为ZIP格式

**详细说明：**

查看完整的独立打包文档：[BUILD_STANDALONE.md](BUILD_STANDALONE.md)

### 配置说明

主要配置文件：`src/main/resources/application.properties`

```properties
# 服务器配置
server.port=8080

# H2数据库配置
spring.datasource.url=jdbc:h2:file:./data/money_manage;AUTO_SERVER=TRUE;MODE=MySQL
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2控制台（开发调试用）
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# MyBatis配置
mybatis.mapper-locations=classpath:mapper/*.xml
mybatis.type-aliases-package=com.susu.domain.entity
mybatis.configuration.map-underscore-to-camel-case=true
```

### API接口文档

启动应用后访问Swagger文档：http://localhost:8080/swagger-ui.html

主要接口模块：

1. **账单管理** (`/api/bills`)
   - `GET /api/bills/page` - 分页查询账单
   - `POST /api/bills` - 添加账单
   - 其他增删改查接口

2. **分类管理** (`/api/categories`)
   - `GET /api/categories` - 获取所有分类
   - `POST /api/categories` - 新增分类
   - `PUT /api/categories/{id}` - 更新分类
   - `DELETE /api/categories/{id}` - 删除分类

3. **规则管理** (`/api/category-rules`)
   - `GET /api/category-rules/page` - 分页查询规则
   - `POST /api/category-rules` - 新增规则
   - `PUT /api/category-rules/{id}` - 更新规则
   - `DELETE /api/category-rules/{id}` - 删除规则

4. **导入管理** (`/api/import`)
   - `POST /api/import/upload` - 上传账单文件
   - `GET /api/import/sessions` - 查询导入批次

5. **统计报表** (`/api/reports`)
   - `GET /api/reports/daily/summary/today` - 今日汇总
   - `GET /api/reports/monthly/summary/current` - 本月汇总
   - `GET /api/reports/statistics/monthly` - 月度统计
   - `GET /api/reports/statistics/quarterly` - 季度统计
   - `GET /api/reports/statistics/category` - 分类统计

### 数据库访问

开发时可以通过H2控制台访问数据库：

```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:file:./data/money_manage
Username: sa
Password: (留空)
```

---

## 📝 常见问题

### Q: 如何修改服务端口？

A: 修改 `application.properties` 文件中的 `server.port` 配置，或在启动时指定：
```bash
java -jar MoneyManageSystem.jar --server.port=9090
```

### Q: 数据库文件在哪里？

A: 数据库文件位于应用运行目录下的 `data` 文件夹中：
```
./data/money_manage.mv.db
```

### Q: 如何备份数据？

A: 直接复制 `data` 文件夹即可备份所有数据。恢复时将文件夹复制回去即可。

### Q: 导入Excel文件失败怎么办？

A: 请确保：
1. 文件格式为微信官方导出的Excel格式
2. 文件没有被加密或损坏
3. 文件大小不超过10MB
4. 检查后台日志查看详细错误信息

### Q: 能否使用MySQL数据库？

A: 可以。修改 `application.properties` 中的数据库配置，取消MySQL配置的注释，并注释掉H2配置即可。

### Q: EXE文件双击没反应？

A: 请检查：
1. 是否安装了Java 17或更高版本
2. 是否配置了JAVA_HOME环境变量
3. 8080端口是否被占用
4. 以管理员身份运行

### Q: 如何让用户无需安装Java即可运行？

A: 使用绿色免安装版（包含JRE）：
1. 下载 `WeChatFinance-Portable.zip`
2. 解压后直接双击 `启动.bat` 即可
3. 开发者可以使用 `build-standalone.bat` 脚本自己打包

详见：[BUILD_STANDALONE.md](BUILD_STANDALONE.md)

---

## 🔄 更新日志

### v0.0.1-SNAPSHOT (2025-10-28)

#### ✨ 新增功能
- ✅ 微信账单Excel导入功能
- ✅ 两级分类管理体系
- ✅ 智能分类规则引擎
- ✅ 导入批次管理
- ✅ 月度/季度/分类统计报表
- ✅ Apple风格UI设计
- ✅ 响应式页面适配
- ✅ H2数据库零配置方案

#### 🐛 修复问题
- ✅ 修复H2数据库SQL兼容性问题
- ✅ 修复月度统计图表显示问题
- ✅ 修复分类查询关联问题

---

## 🤝 贡献指南

欢迎提交Issue和Pull Request！

1. Fork本仓库
2. 创建特性分支：`git checkout -b feature/AmazingFeature`
3. 提交改动：`git commit -m 'Add some AmazingFeature'`
4. 推送到分支：`git push origin feature/AmazingFeature`
5. 提交Pull Request

### 贡献建议

- 🐛 提交Bug报告
- 💡 提出新功能建议
- 📝 改进文档
- 🎨 优化UI/UX
- ⚡ 性能优化
- 🌐 多语言支持

---

## 📄 开源协议

本项目采用 [MIT License](LICENSE) 开源协议。

---

## 📞 联系方式

- **项目地址**: https://github.com/IsSusuuuu/wechat_bill_manage
- **问题反馈**: https://github.com/IsSusuuuu/wechat_bill_manage/issues
- **邮箱**: 1816145509@qq.com

---

## 🙏 致谢

感谢以下开源项目：

- [Spring Boot](https://spring.io/projects/spring-boot) - 强大的Java应用框架
- [MyBatis](https://mybatis.org/) - 优秀的持久层框架
- [H2 Database](https://www.h2database.com/) - 轻量级数据库
- [Apache POI](https://poi.apache.org/) - Excel文件处理
- [Chart.js](https://www.chartjs.org/) - 图表可视化
- [PageHelper](https://github.com/pagehelper/Mybatis-PageHelper) - MyBatis分页插件

---

<div align="center">

**⭐ 如果这个项目对你有帮助，请给一个Star！⭐**

Made with ❤️ by 苏苏

</div>

