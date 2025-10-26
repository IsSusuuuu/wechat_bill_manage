@echo off
chcp 65001 > nul
echo ========================================
echo   微信财务监控系统 - 打包脚本
echo ========================================
echo.

set VERSION=v1.0.0
set RELEASE_DIR=微信财务监控系统_%VERSION%
set JAR_NAME=MoneyManageSystem-0.0.1-SNAPSHOT.jar

echo [1/6] 清理旧的构建文件...
if exist target\%RELEASE_DIR% rmdir /s /q target\%RELEASE_DIR%
if exist target\%RELEASE_DIR%.zip del target\%RELEASE_DIR%.zip

echo [2/6] 编译和打包项目...
call mvn clean package -DskipTests
if errorlevel 1 (
    echo 构建失败！请检查错误信息。
    pause
    exit /b 1
)

echo [3/6] 创建发布目录...
mkdir target\%RELEASE_DIR%
mkdir target\%RELEASE_DIR%\启动程序
mkdir target\%RELEASE_DIR%\配置

echo [4/6] 复制文件...
copy target\%JAR_NAME% target\%RELEASE_DIR%\启动程序\money-manage-system.jar
copy src\main\resources\application.properties target\%RELEASE_DIR%\配置\
if exist money.db copy money.db target\%RELEASE_DIR%\启动程序\

echo [5/6] 创建启动脚本...
echo @echo off > target\%RELEASE_DIR%\启动程序\start.bat
echo chcp 65001 ^> nul >> target\%RELEASE_DIR%\启动程序\start.bat
echo echo 正在启动微信财务监控系统... >> target\%RELEASE_DIR%\启动程序\start.bat
echo echo. >> target\%RELEASE_DIR%\启动程序\start.bat
echo echo 启动后请在浏览器访问: http://localhost:8080 >> target\%RELEASE_DIR%\启动程序\start.bat
echo echo 按 Ctrl+C 停止服务 >> target\%RELEASE_DIR%\启动程序\start.bat
echo echo. >> target\%RELEASE_DIR%\启动程序\start.bat
echo java -jar money-manage-system.jar >> target\%RELEASE_DIR%\启动程序\start.bat
echo pause >> target\%RELEASE_DIR%\启动程序\start.bat

echo [6/6] 创建使用说明...
echo ======================================== > target\%RELEASE_DIR%\使用说明.txt
echo   微信财务监控系统 %VERSION% >> target\%RELEASE_DIR%\使用说明.txt
echo ======================================== >> target\%RELEASE_DIR%\使用说明.txt
echo. >> target\%RELEASE_DIR%\使用说明.txt
echo 【系统要求】 >> target\%RELEASE_DIR%\使用说明.txt
echo - Windows 7/8/10/11 >> target\%RELEASE_DIR%\使用说明.txt
echo - Java 17 或更高版本 >> target\%RELEASE_DIR%\使用说明.txt
echo - MySQL 5.7 或更高版本 >> target\%RELEASE_DIR%\使用说明.txt
echo. >> target\%RELEASE_DIR%\使用说明.txt
echo 【快速开始】 >> target\%RELEASE_DIR%\使用说明.txt
echo 1. 确保已安装Java 17 >> target\%RELEASE_DIR%\使用说明.txt
echo 2. 配置MySQL数据库（修改配置\application.properties） >> target\%RELEASE_DIR%\使用说明.txt
echo 3. 双击"启动程序\start.bat"启动系统 >> target\%RELEASE_DIR%\使用说明.txt
echo 4. 浏览器访问: http://localhost:8080 >> target\%RELEASE_DIR%\使用说明.txt
echo. >> target\%RELEASE_DIR%\使用说明.txt
echo 【配置说明】 >> target\%RELEASE_DIR%\使用说明.txt
echo 1. 打开 配置\application.properties >> target\%RELEASE_DIR%\使用说明.txt
echo 2. 修改数据库连接信息 >> target\%RELEASE_DIR%\使用说明.txt
echo. >> target\%RELEASE_DIR%\使用说明.txt
echo 【常见问题】 >> target\%RELEASE_DIR%\使用说明.txt
echo Q: 提示"端口被占用" >> target\%RELEASE_DIR%\使用说明.txt
echo A: 修改配置文件的server.port值 >> target\%RELEASE_DIR%\使用说明.txt
echo. >> target\%RELEASE_DIR%\使用说明.txt
echo Q: 无法连接数据库 >> target\%RELEASE_DIR%\使用说明.txt
echo A: 检查MySQL服务是否启动，配置是否正确 >> target\%RELEASE_DIR%\使用说明.txt

echo.
echo ========================================
echo   打包完成！
echo ========================================
echo.
echo 发布文件位置: target\%RELEASE_DIR%
echo.
echo 下一步操作:
echo 1. 检查 target\%RELEASE_DIR% 目录中的文件
echo 2. 将目录打包成zip文件
echo 3. 在GitHub创建Release并上传zip文件
echo.
pause
