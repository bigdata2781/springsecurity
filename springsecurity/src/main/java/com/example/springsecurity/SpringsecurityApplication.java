package com.example.springsecurity;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringsecurityApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(); 
		app.setDefaultProperties(Collections
		          .singletonMap("spring.profiles.active", "local"));
		app.run(SpringsecurityApplication.class, args);

	}

}
