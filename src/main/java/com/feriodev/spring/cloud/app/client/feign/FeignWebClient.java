package com.feriodev.spring.cloud.app.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.feriodev.spring.cloud.app.client.model.Product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@FeignClient(name = "service-product")
public interface FeignWebClient {

	@GetMapping("/list")
	public Flux<Product> list();
	
	@GetMapping("/detail/{id}")
	public Mono<Product> detail(@PathVariable String id);
}
