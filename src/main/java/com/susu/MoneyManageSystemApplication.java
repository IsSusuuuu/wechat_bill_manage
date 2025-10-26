package com.susu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.susu")
@MapperScan("com.susu.mapper")
public class MoneyManageSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneyManageSystemApplication.class, args);
	}
}


