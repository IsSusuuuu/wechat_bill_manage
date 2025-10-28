@echo off
chcp 65001 >nul
title 微信财务监控系统

echo ==========================================
echo   微信财务监控系统 v1.0.0
echo   正在启动，请稍候...
echo ==========================================
echo.

cd /d "%~dp0"

REM 检查是否存在内置JRE
if exist "jre\bin\java.exe" (
    echo 使用内置JRE启动...
    set JAVA_CMD=jre\bin\java.exe
) else (
    REM 使用系统Java
    echo 使用系统Java启动...
    set JAVA_CMD=java
)

REM 检查JAR文件
if exist "target\MoneyManageSystem-0.0.1-SNAPSHOT.jar" (
    set JAR_FILE=target\MoneyManageSystem-0.0.1-SNAPSHOT.jar
) else if exist "app\MoneyManageSystem-0.0.1-SNAPSHOT.jar" (
    set JAR_FILE=app\MoneyManageSystem-0.0.1-SNAPSHOT.jar
) else if exist "MoneyManageSystem-0.0.1-SNAPSHOT.jar" (
    set JAR_FILE=MoneyManageSystem-0.0.1-SNAPSHOT.jar
) else (
    echo [错误] 找不到应用程序JAR文件
    echo 请确保JAR文件存在于以下位置之一：
    echo   - target\MoneyManageSystem-0.0.1-SNAPSHOT.jar
    echo   - app\MoneyManageSystem-0.0.1-SNAPSHOT.jar
    echo   - MoneyManageSystem-0.0.1-SNAPSHOT.jar
    pause
    exit /b 1
)

echo 启动文件：%JAR_FILE%
echo.
echo 应用启动成功后，请在浏览器访问：
echo   http://localhost:8080/index.html
echo.
echo 按 Ctrl+C 可停止程序
echo ==========================================
echo.

REM 启动应用
%JAVA_CMD% -jar "%JAR_FILE%"

if errorlevel 1 (
    echo.
    echo ==========================================
    echo   [错误] 应用启动失败
    echo ==========================================
    echo.
    echo 可能的原因：
    echo   1. 未安装Java或Java版本低于17
    echo   2. 8080端口已被占用
    echo   3. JAR文件损坏
    echo.
    echo 解决方法：
    echo   1. 安装JDK 17或更高版本
    echo   2. 关闭占用8080端口的程序
    echo   3. 重新下载或打包JAR文件
    echo.
    pause
)

