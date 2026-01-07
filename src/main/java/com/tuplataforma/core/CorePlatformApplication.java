package com.tuplataforma.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.tuplataforma.core")
@EntityScan(basePackages = "com.tuplataforma.core")
public class CorePlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(CorePlatformApplication.class, args);
	}

}
