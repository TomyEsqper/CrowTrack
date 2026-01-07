package com.tuplataforma.core.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.tuplataforma.core")
public class CorePlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(CorePlatformApplication.class, args);
	}

}
