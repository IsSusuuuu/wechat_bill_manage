# 微信财务监控系统 - 打包和发布指南

本指南将帮助你将项目打包成可执行的exe文件，并发布到GitHub供他人下载。

## 📋 前提条件

- Java 17 或更高版本
- Maven 3.6+
- MySQL 数据库
- (可选) Launch4j 用于生成Windows exe

## 🔨 方案一：直接使用JAR文件（推荐）

### 1. 打包项目

在项目根目录执行：

```bash
mvn clean package
```

这会在 `target` 目录生成：
- `MoneyManageSystem-0.0.1-SNAPSHOT.jar` - 可执行JAR文件

### 2. 准备发布文件

创建一个发布文件夹，包含以下内容：

```
微信财务监控系统_v1.0/
├── money-manage-system.jar          # 重命名后的JAR文件
├── money.db                          # SQLite数据库文件（如果需要）
├── config/
│   └── application.properties       # 配置文件
├── README.md                         # 使用说明
├── start.bat                         # Windows启动脚本
└── LICENSE                           # 许可证文件
```

### 3. 创建启动脚本

**start.bat** (Windows):
```batch
@echo off
chcp 65001 > nul
echo 正在启动微信财务监控系统...
echo.
echo 启动后请在浏览器访问: http://localhost:8080
echo 按 Ctrl+C 停止服务
echo.
java -jar money-manage-system.jar
pause
```

### 4. 创建配置文件

**application.properties** (config目录):
```properties
# 服务器端口
server.port=8080

# 数据库配置（根据实际情况修改）
spring.datasource.url=jdbc:mysql://localhost:3306/wechat_finance?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=your_password

# MyBatis配置
mybatis.configuration.map-underscore-to-camel-case=true
mybatis.mapper-locations=classpath:mapper/*.xml

# 上传文件大小限制
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

## 🪟 方案二：使用Launch4j生成EXE（Windows专用）

### 1. 下载并安装Launch4j

从 [Launch4j官网](http://launch4j.sourceforge.net/) 下载并安装。

### 2. 配置Launch4j

1. 打开Launch4j
2. 基本设置：
   - **Output file**: `微信财务监控系统.exe`
   - **Jar**: 选择打包好的JAR文件
   - **Minimum JRE version**: `1.8`

3. JRE设置：
   - **JRE search strategy**: Use bundled JRE
   - (可选) **Bundled JRE path**: 指定JRE路径

4. 生成exe文件

### 3. 打包发布

创建文件夹：
```
微信财务监控系统_v1.0/
├── 微信财务监控系统.exe
├── JRE/                    # (如果需要内置JRE)
├── config/
│   └── application.properties
├── README.md
└── LICENSE
```

## 📦 方案三：使用jpackage（Java 14+，推荐新方案）

如果你使用Java 14或更高版本：

```bash
# 创建临时目录
mkdir target/app

# 复制JAR和依赖
cp target/MoneyManageSystem-0.0.1-SNAPSHOT.jar target/app/

# 使用jpackage创建安装包
jpackage --input target/app \
  --name "微信财务监控系统" \
  --main-jar MoneyManageSystem-0.0.1-SNAPSHOT.jar \
  --type msi \
  --dest target/release
```

## 🚀 发布到GitHub

### 1. 创建Release

1. 访问你的GitHub仓库
2. 点击 **Releases** -> **Create a new release**
3. 填写版本号（如 `v1.0.0`）
4. 添加发布说明

### 2. 上传文件

将以下文件打包成zip上传：
- `微信财务监控系统.exe` 或 JAR文件
- 配置文件
- 使用说明

### 3. 推荐的文件结构

```
微信财务监控系统_v1.0.0.zip
├── 启动程序/
│   ├── 微信财务监控系统.exe (或 .jar)
│   └── start.bat
├── 配置/
│   └── application.properties
├── 数据库/
│   └── money.db (如果需要)
├── 使用说明.txt
└── README.md
```

## 📝 使用说明模板

创建 **使用说明.txt**:

```
========================================
  微信财务监控系统 v1.0
========================================

【系统要求】
- Windows 7/8/10/11
- Java 17 或更高版本
- MySQL 5.7 或更高版本

【快速开始】
1. 确保已安装Java 17
2. 配置MySQL数据库（修改config/application.properties）
3. 双击"启动程序.exe"或运行start.bat
4. 浏览器访问: http://localhost:8080

【配置说明】
1. 打开 config/application.properties
2. 修改数据库连接信息：
   - spring.datasource.url
   - spring.datasource.username
   - spring.datasource.password
3. 修改端口（如果需要）：server.port=8080

【常见问题】
Q: 提示"端口被占用"
A: 修改配置文件的server.port值

Q: 无法连接数据库
A: 检查MySQL服务是否启动，配置是否正确

【技术支持】
GitHub: https://github.com/your-username/MoneyManageSystem
Issues: https://github.com/your-username/MoneyManageSystem/issues
```

## 🏷️ 版本管理建议

在README.md中添加下载链接：

```markdown
## 📥 下载

- [最新版本 v1.0.0](https://github.com/your-username/MoneyManageSystem/releases/tag/v1.0.0)
- 需要Java 17或更高版本

## ⚡ 快速开始

1. 下载并解压 `微信财务监控系统_v1.0.0.zip`
2. 修改 `config/application.properties` 中的数据库配置
3. 运行 `微信财务监控系统.exe` 或 `start.bat`
4. 浏览器访问 http://localhost:8080
```

## 📌 额外建议

1. **数据库初始化脚本**: 提供一个SQL脚本来初始化数据库
2. **示例数据**: 可以包含一些示例数据方便测试
3. **一键安装**: 考虑使用NSIS或Inno Setup创建安装程序
4. **自动更新**: 添加检查更新功能
5. **日志文件**: 指导用户查看日志文件便于问题排查

## 🎯 完整发布检查清单

- [ ] 代码测试通过
- [ ] 配置文件模板准备
- [ ] 使用说明文档完善
- [ ] 许可证文件添加
- [ ] 版本号更新
- [ ] 打包文件和目录检查
- [ ] 压缩包创建
- [ ] GitHub Release创建
- [ ] README.md更新下载链接

---

祝你发布顺利！🎉
