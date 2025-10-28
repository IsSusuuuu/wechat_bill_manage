# 构建独立运行包（无需安装Java）

本文档介绍如何打包应用程序，使用户无需安装Java即可运行。

---

## 🎯 方案一：jpackage 打包（推荐）

### 适用场景
- 需要创建专业的安装包
- 需要程序集成到系统（开始菜单、桌面快捷方式等）
- 适合分发给普通用户

### 前置要求
- JDK 17+ （开发机需要，用户机不需要）
- Windows: 需要安装 WiX Toolset 3.11+（用于生成.msi安装包）
- Linux: 需要安装 rpm-build 或 dpkg-dev

### 步骤

#### 1. 打包JAR文件
```bash
mvn clean package -DskipTests
```

#### 2. 使用jpackage创建安装包

##### Windows (MSI安装包)
```bash
jpackage ^
  --input target ^
  --name "微信财务监控系统" ^
  --main-jar MoneyManageSystem-0.0.1-SNAPSHOT.jar ^
  --main-class com.susu.MoneyManageSystemApplication ^
  --type msi ^
  --win-dir-chooser ^
  --win-menu ^
  --win-shortcut ^
  --icon src/main/resources/icon.ico ^
  --app-version 1.0.0 ^
  --vendor "苏苏" ^
  --description "微信账单管理与财务分析系统"
```

##### Windows (EXE安装包)
```bash
jpackage ^
  --input target ^
  --name "微信财务监控系统" ^
  --main-jar MoneyManageSystem-0.0.1-SNAPSHOT.jar ^
  --main-class com.susu.MoneyManageSystemApplication ^
  --type exe ^
  --win-dir-chooser ^
  --win-menu ^
  --win-shortcut ^
  --app-version 1.0.0 ^
  --vendor "苏苏"
```

##### Linux (DEB包)
```bash
jpackage \
  --input target \
  --name wechat-finance \
  --main-jar MoneyManageSystem-0.0.1-SNAPSHOT.jar \
  --main-class com.susu.MoneyManageSystemApplication \
  --type deb \
  --linux-shortcut \
  --app-version 1.0.0 \
  --vendor "苏苏"
```

##### macOS (DMG)
```bash
jpackage \
  --input target \
  --name "微信财务监控系统" \
  --main-jar MoneyManageSystem-0.0.1-SNAPSHOT.jar \
  --main-class com.susu.MoneyManageSystemApplication \
  --type dmg \
  --mac-package-name "WeChatFinance" \
  --app-version 1.0.0 \
  --vendor "苏苏"
```

#### 3. 参数说明

| 参数 | 说明 |
|------|------|
| `--input` | JAR文件所在目录 |
| `--name` | 应用程序名称 |
| `--main-jar` | 主JAR文件名 |
| `--main-class` | 主类（带包名） |
| `--type` | 安装包类型（msi/exe/deb/dmg等） |
| `--win-dir-chooser` | 允许用户选择安装目录 |
| `--win-menu` | 创建开始菜单快捷方式 |
| `--win-shortcut` | 创建桌面快捷方式 |
| `--icon` | 应用程序图标 |
| `--app-version` | 应用版本号 |
| `--vendor` | 供应商名称 |

### 优点
✅ 包含完整JRE，用户无需安装Java  
✅ 专业的安装程序，用户体验好  
✅ 自动创建快捷方式  
✅ 支持卸载  
✅ 文件关联（可选）  

### 缺点
❌ 打包文件较大（约50-100MB）  
❌ 需要为每个平台单独打包  

---

## 🎯 方案二：手动打包JRE（简单快速）

### 适用场景
- 快速打包，无需复杂配置
- 绿色免安装版本
- 适合技术用户

### 步骤

#### 1. 下载JRE 17
从Oracle或AdoptOpenJDK下载JRE 17：
- Oracle: https://www.oracle.com/java/technologies/downloads/#java17
- Adoptium: https://adoptium.net/

#### 2. 创建发布目录结构
```
WeChatFinance-Portable/
├── jre/                    # JRE运行环境
│   ├── bin/
│   ├── lib/
│   └── ...
├── app/                    # 应用程序
│   └── MoneyManageSystem-0.0.1-SNAPSHOT.jar
├── data/                   # 数据目录（可选，首次运行会创建）
├── 启动.bat                # Windows启动脚本
├── 启动.sh                 # Linux/Mac启动脚本
└── README.txt             # 使用说明
```

#### 3. 创建启动脚本

##### Windows: `启动.bat`
```batch
@echo off
chcp 65001 >nul
title 微信财务监控系统

echo ======================================
echo   微信财务监控系统
echo   正在启动，请稍候...
echo ======================================
echo.

cd /d "%~dp0"

REM 检查JRE是否存在
if not exist "jre\bin\java.exe" (
    echo [错误] 找不到Java运行环境
    echo 请确保jre文件夹存在且完整
    pause
    exit /b 1
)

REM 启动应用
jre\bin\java.exe -jar app\MoneyManageSystem-0.0.1-SNAPSHOT.jar

if errorlevel 1 (
    echo.
    echo [错误] 应用启动失败
    pause
)
```

##### Linux/macOS: `启动.sh`
```bash
#!/bin/bash

echo "======================================"
echo "  微信财务监控系统"
echo "  正在启动，请稍候..."
echo "======================================"
echo

cd "$(dirname "$0")"

# 检查JRE是否存在
if [ ! -f "jre/bin/java" ]; then
    echo "[错误] 找不到Java运行环境"
    echo "请确保jre文件夹存在且完整"
    exit 1
fi

# 启动应用
./jre/bin/java -jar app/MoneyManageSystem-0.0.1-SNAPSHOT.jar

if [ $? -ne 0 ]; then
    echo
    echo "[错误] 应用启动失败"
    read -p "按回车键退出..."
fi
```

给脚本添加执行权限：
```bash
chmod +x 启动.sh
```

#### 4. 创建README.txt
```text
==========================================
    微信财务监控系统 v1.0.0
    绿色免安装版
==========================================

【运行方法】
  Windows: 双击 "启动.bat"
  Linux/Mac: 运行 "./启动.sh"

【首次使用】
  1. 启动程序后，浏览器会自动打开
  2. 如未自动打开，请手动访问：
     http://localhost:8080/index.html

【系统要求】
  - 操作系统：Windows 7+, Linux, macOS
  - 内存：建议512MB以上
  - 端口：8080（可在配置中修改）

【数据位置】
  所有数据保存在 data 文件夹中
  备份时请复制整个 data 文件夹

【注意事项】
  - 不要删除 jre 文件夹
  - 不要移动或重命名 app 文件夹中的JAR文件
  - 如需修改配置，编辑 application.properties

【联系方式】
  项目地址：https://github.com/IsSusuuuu/wechat_bill_manage
  问题反馈：https://github.com/IsSusuuuu/wechat_bill_manage/issues
  邮箱：1816145509@qq.com

==========================================
```

#### 5. 打包压缩
将整个文件夹压缩为ZIP或7z格式，即可分发。

### 优点
✅ 操作简单，易于理解  
✅ 绿色免安装，解压即用  
✅ 数据位置明确，便于备份  
✅ 可以快速迭代更新（只需替换JAR）  

### 缺点
❌ 包含完整JRE，文件较大  
❌ 没有集成到系统（无快捷方式）  
❌ 手动操作较多  

---

## 🎯 方案三：使用jlink创建精简JRE（高级）

### 适用场景
- 需要最小化分发包大小
- 对启动速度有要求
- 愿意投入时间优化

### 步骤

#### 1. 分析依赖模块
```bash
jdeps --list-deps --ignore-missing-deps target\MoneyManageSystem-0.0.1-SNAPSHOT.jar
```

#### 2. 创建自定义JRE
```bash
jlink --add-modules java.base,java.logging,java.sql,java.desktop,java.management,java.naming,java.xml ^
      --output custom-jre ^
      --strip-debug ^
      --no-header-files ^
      --no-man-pages ^
      --compress=2
```

#### 3. 使用自定义JRE
将生成的 `custom-jre` 文件夹放入发布目录，按方案二创建启动脚本。

### 优点
✅ JRE体积大幅减小（可能减小50%以上）  
✅ 启动速度更快  
✅ 只包含必要模块，更安全  

### 缺点
❌ 配置复杂，需要准确了解依赖  
❌ 模块遗漏可能导致运行时错误  
❌ 每次依赖变化都需要重新配置  

---

## 📦 推荐方案对比

| 方案 | 难度 | 文件大小 | 用户体验 | 适用场景 |
|------|------|----------|----------|----------|
| jpackage | 中等 | 大 (50-100MB) | ⭐⭐⭐⭐⭐ | 正式发布 |
| 手动打包JRE | 简单 | 大 (60-120MB) | ⭐⭐⭐⭐ | 快速分发 |
| jlink精简 | 困难 | 中 (30-60MB) | ⭐⭐⭐⭐ | 技术用户 |

---

## 🚀 快速开始脚本（Windows）

创建 `build-standalone.bat` 一键打包脚本：

```batch
@echo off
chcp 65001 >nul
echo ==========================================
echo   微信财务监控系统 - 独立打包工具
echo ==========================================
echo.

REM 1. 打包JAR
echo [1/4] 正在打包JAR文件...
call mvn clean package -DskipTests
if errorlevel 1 goto error

REM 2. 创建发布目录
echo [2/4] 创建发布目录...
if exist "release" rd /s /q release
mkdir release\WeChatFinance-Portable
mkdir release\WeChatFinance-Portable\app
mkdir release\WeChatFinance-Portable\jre

REM 3. 复制JAR
echo [3/4] 复制应用文件...
copy target\MoneyManageSystem-0.0.1-SNAPSHOT.jar release\WeChatFinance-Portable\app\

REM 4. 提示复制JRE
echo [4/4] 请手动操作：
echo   1. 下载JRE 17
echo   2. 解压到 release\WeChatFinance-Portable\jre 目录
echo   3. 创建启动脚本（参考文档）
echo.

echo ==========================================
echo   打包完成！
echo   输出目录：release\WeChatFinance-Portable
echo ==========================================
pause
exit /b 0

:error
echo.
echo [错误] 打包失败，请检查错误信息
pause
exit /b 1
```

---

## 💡 建议

1. **开发阶段**：使用方案二（手动打包JRE），快速测试
2. **正式发布**：使用方案一（jpackage），提供专业安装包
3. **绿色版本**：使用方案二，提供免安装版供用户选择

---

## ⚠️ 注意事项

### 许可证问题
- Oracle JRE: 商业使用需要许可证
- 建议使用 OpenJDK/Adoptium: 完全免费，可商用

### 文件大小
- 完整JRE: 约60-100MB
- 精简JRE (jlink): 约30-60MB
- 应用JAR: 约20-40MB

### 更新维护
- 使用jpackage: 需要重新安装
- 绿色版本: 只需替换JAR文件即可

---

## 📞 需要帮助？

如有问题，请查看：
- 项目文档：https://github.com/IsSusuuuu/wechat_bill_manage
- 提交Issue：https://github.com/IsSusuuuu/wechat_bill_manage/issues

