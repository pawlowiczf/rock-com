package com.roc.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
class PrintEnvVariables implements CommandLineRunner {

	@Autowired
	private Environment env;

	@Override
	public void run(String... args) {
		System.out.println("SPRING_PROFILES_ACTIVE: " + env.getProperty("spring.profiles.active"));
		System.out.println("DATASOURCE URL: " + env.getProperty("spring.datasource.url"));
		System.out.println("DATASOURCE USERNAME: " + env.getProperty("spring.datasource.username"));
		System.out.println("DATASOURCE PASSWORD: " + env.getProperty("spring.datasource.password"));
	}
}

@SpringBootApplication
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

}
