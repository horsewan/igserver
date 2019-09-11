package com.dy.igserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IgserverApplication {
	public static void main(String[] args) {
		SpringApplication.run(IgserverApplication.class, args);
		System.out.println("服务启动完成....................");
	}
}
