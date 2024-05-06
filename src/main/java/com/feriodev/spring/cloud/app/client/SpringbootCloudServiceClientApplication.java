package com.feriodev.spring.cloud.app.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SpringbootCloudServiceClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootCloudServiceClientApplication.class, args);
	}

}
