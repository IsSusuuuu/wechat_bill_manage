# 微信财务监控系统

一个基于 Spring Boot 开发的微信账单管理与分析系统，支持账单导入、分类管理、规则配置和统计分析。

## ✨ 主要功能

- 📊 **账单管理**: 导入和管理微信账单数据
- 🏷️ **分类管理**: 自定义收支分类体系
- 🤖 **自动分类**: 基于规则的自动分类功能
- 📈 **统计分析**: 月度、季度、分类统计报表
- 🎨 **现代化UI**: 采用Apple风格的界面设计
- 📄 **分页查询**: 高效的数据分页展示

## 📥 下载

### 最新版本

- [v1.0.0 下载](https://github.com/your-username/MoneyManageSystem/releases/tag/v1.0.0)
- 需要 Java 17 或更高版本

### 系统要求

- **操作系统**: Windows 7/8/10/11, macOS, Linux
- **Java**: JDK 17 或更高版本
- **数据库**: MySQL 5.7 或更高版本
- **内存**: 建议 512MB 以上

## ⚡ 快速开始

### 1. 下载并解压

下载最新的发布包并解压到任意目录。

### 2. 配置数据库

编辑 `配置/application.properties` 文件，修改数据库连接信息：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/wechat_finance
spring.datasource.username=root
spring.datasource.password=your_password
```

### 3. 初始化数据库

创建数据库并执行SQL脚本（如果需要）：

```sql
CREATE DATABASE wechat_finance CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 4. 启动系统

**Windows:**
```bash
cd 启动程序
start.bat
```

**Linux/macOS:**
```bash
cd 启动程序
java -jar money-manage-system.jar
```

### 5. 访问系统

浏览器打开: http://localhost:8080

## 📖 使用指南

### 账单导入

1. 进入 **账单导入** 页面
2. 选择微信导出的账单 Excel 文件
3. 点击上传，系统自动解析并导入

### 分类管理

1. 进入 **收支分类** 页面
2. 点击 **新增分类** 创建分类
3. 支持一级和二级分类层级

### 规则配置

1. 进入 **自动填充规则** 页面
2. 创建匹配规则，设置关键词和分类
3. 系统会根据规则自动对账单进行分类

### 数据统计

1. 进入 **分析报表** 页面
2. 查看月度、季度统计图表
3. 查看分类支出的饼图分析

## 🛠️ 开发指南

### 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 5.7+
- IDE (推荐 IntelliJ IDEA)

### 本地运行

```bash
# 克隆项目
git clone https://github.com/your-username/MoneyManageSystem.git

# 进入项目目录
cd MoneyManageSystem

# 安装依赖
mvn clean install

# 运行项目
mvn spring-boot:run
```

### 打包发布

```bash
# 使用提供的打包脚本
build-release.bat

# 或手动打包
mvn clean package
```

详细打包说明请查看 [BUILD_AND_RELEASE.md](./BUILD_AND_RELEASE.md)

## 📁 项目结构

```
MoneyManageSystem/
├── src/main/java/com/susu/
│   ├── controller/          # 控制器层
│   ├── service/             # 服务层
│   ├── mapper/              # 数据访问层
│   ├── domain/              # 实体和DTO
│   └── config/              # 配置类
├── src/main/resources/
│   ├── mapper/              # MyBatis XML
│   ├── static/              # 前端静态文件
│   └── application.properties
└── pom.xml
```

## 🔧 配置说明

主要配置项（`application.properties`）：

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| server.port | 服务器端口 | 8080 |
| spring.datasource.url | 数据库连接地址 | - |
| spring.datasource.username | 数据库用户名 | root |
| spring.datasource.password | 数据库密码 | - |
| mybatis.mapper-locations | Mapper XML路径 | classpath:mapper/*.xml |

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交改动 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 MIT 许可证。详见 [LICENSE](./LICENSE) 文件。

## 📞 联系

- GitHub Issues: [提交问题](https://github.com/your-username/MoneyManageSystem/issues)
- 项目主页: [访问仓库](https://github.com/your-username/MoneyManageSystem)

## 🙏 致谢

感谢所有为本项目做出贡献的开发者！

---

⭐ 如果这个项目对你有帮助，请给一个 Star！
