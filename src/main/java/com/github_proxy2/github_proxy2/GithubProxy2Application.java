package com.github_proxy2.github_proxy2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class GithubProxy2Application {

	public static void main(String[] args) {
		SpringApplication.run(GithubProxy2Application.class, args);
	}
}
