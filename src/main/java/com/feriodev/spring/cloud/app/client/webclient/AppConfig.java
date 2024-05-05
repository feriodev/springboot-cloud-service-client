package com.feriodev.spring.cloud.app.client.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {
	
	@Value("${context.endpoint.product}")
	private String endpoint;

    @Bean
	@LoadBalanced
	WebClient.Builder registrarWebClient() {
		return WebClient.builder().baseUrl(endpoint)        
				.defaultHeaders(httpHeaders -> httpHeaders.setContentType(MediaType.APPLICATION_JSON));
	}
}
