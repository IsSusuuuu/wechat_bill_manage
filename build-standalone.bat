@echo off
chcp 65001 >nul
echo ==========================================
echo   微信财务监控系统 - 独立打包工具
echo   创建包含JRE的绿色免安装版
echo ==========================================
echo.

REM 1. 打包JAR
echo [步骤 1/5] 正在打包JAR文件...
call mvn clean package -DskipTests
if errorlevel 1 goto error
echo ✓ JAR打包完成
echo.

REM 2. 创建发布目录
echo [步骤 2/5] 创建发布目录...
if exist "release" rd /s /q release
mkdir release\WeChatFinance-Portable
mkdir release\WeChatFinance-Portable\app
mkdir release\WeChatFinance-Portable\jre
echo ✓ 目录创建完成
echo.

REM 3. 复制JAR
echo [步骤 3/5] 复制应用文件...
copy target\MoneyManageSystem-0.0.1-SNAPSHOT.jar release\WeChatFinance-Portable\app\
echo ✓ 应用文件复制完成
echo.

REM 4. 创建启动脚本
echo [步骤 4/5] 创建启动脚本...
(
echo @echo off
echo chcp 65001 ^>nul
echo title 微信财务监控系统
echo.
echo echo ==========================================
echo echo   微信财务监控系统
echo echo   正在启动，请稍候...
echo echo ==========================================
echo echo.
echo.
echo cd /d "%%~dp0"
echo.
echo REM 检查JRE是否存在
echo if not exist "jre\bin\java.exe" ^(
echo     echo [错误] 找不到Java运行环境
echo     echo 请确保jre文件夹存在且完整
echo     pause
echo     exit /b 1
echo ^)
echo.
echo echo 应用启动成功后，请在浏览器访问：
echo echo   http://localhost:8080/index.html
echo echo.
echo echo 按 Ctrl+C 可停止程序
echo echo ==========================================
echo echo.
echo.
echo REM 启动应用
echo jre\bin\java.exe -jar app\MoneyManageSystem-0.0.1-SNAPSHOT.jar
echo.
echo if errorlevel 1 ^(
echo     echo.
echo     echo [错误] 应用启动失败
echo     pause
echo ^)
) > release\WeChatFinance-Portable\启动.bat

REM 5. 创建README
(
echo ==========================================
echo     微信财务监控系统 v1.0.0
echo     绿色免安装版
echo ==========================================
echo.
echo 【运行方法】
echo   双击 "启动.bat" 即可
echo.
echo 【首次使用】
echo   1. 启动程序后，浏览器会自动打开
echo   2. 如未自动打开，请手动访问：
echo      http://localhost:8080/index.html
echo.
echo 【系统要求】
echo   - 操作系统：Windows 7+
echo   - 内存：建议512MB以上
echo   - 端口：8080（可在配置中修改）
echo.
echo 【数据位置】
echo   所有数据保存在 data 文件夹中
echo   备份时请复制整个 data 文件夹
echo.
echo 【注意事项】
echo   - 不要删除 jre 文件夹
echo   - 不要移动或重命名 app 文件夹中的JAR文件
echo.
echo 【联系方式】
echo   项目地址：https://github.com/IsSusuuuu/wechat_bill_manage
echo   问题反馈：https://github.com/IsSusuuuu/wechat_bill_manage/issues
echo   邮箱：1816145509@qq.com
echo.
echo ==========================================
) > release\WeChatFinance-Portable\README.txt

echo ✓ 启动脚本和说明文档创建完成
echo.

echo [步骤 5/5] 等待手动操作
echo.
echo ==========================================
echo   打包基本完成！
echo ==========================================
echo.
echo 接下来请手动完成以下步骤：
echo.
echo 1. 下载JRE 17（如果还没有）
echo    推荐下载地址：
echo    https://adoptium.net/temurin/releases/?version=17
echo.
echo 2. 解压JRE到以下目录：
echo    release\WeChatFinance-Portable\jre
echo.
echo    目录结构应该是：
echo    release\WeChatFinance-Portable\
echo      ├── jre\
echo      │   ├── bin\
echo      │   ├── lib\
echo      │   └── ...
echo      ├── app\
echo      │   └── MoneyManageSystem-0.0.1-SNAPSHOT.jar
echo      ├── 启动.bat
echo      └── README.txt
echo.
echo 3. 完成后，将 WeChatFinance-Portable 文件夹
echo    压缩为ZIP格式，即可分发给用户
echo.
echo ==========================================
echo.
echo 是否现在打开release文件夹？
choice /C YN /M "打开release文件夹"
if errorlevel 2 goto end
if errorlevel 1 start explorer release
goto end

:error
echo.
echo ==========================================
echo   [错误] 打包失败
echo ==========================================
echo.
echo 请检查：
echo   1. 是否正确安装Maven
echo   2. 是否在项目根目录执行
echo   3. pom.xml文件是否存在
echo.
pause
exit /b 1

:end
echo.
echo 打包脚本执行完毕
pause

