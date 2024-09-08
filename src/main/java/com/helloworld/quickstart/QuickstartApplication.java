package com.helloworld.quickstart;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.helloworld.quickstart.mapper")
public class QuickstartApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuickstartApplication.class, args);
	}

}
