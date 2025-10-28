package com.susu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication(scanBasePackages = "com.susu")
@MapperScan("com.susu.mapper")
public class MoneyManageSystemApplication {

	public static void main(String[] args) {
		// 创建数据目录
		File dataDir = new File("./data");
		if (!dataDir.exists()) {
			dataDir.mkdirs();
			System.out.println("✅ 数据目录已创建: " + dataDir.getAbsolutePath());
		}
		
		SpringApplication.run(MoneyManageSystemApplication.class, args);
		
		System.out.println("========================================");
		System.out.println("✅ 系统启动成功！");
		System.out.println("🌐 访问地址: http://localhost:8080");
		System.out.println("📊 数据存储: ./data/");
		System.out.println("========================================");
	}
}


