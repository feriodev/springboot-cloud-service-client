package com.feriodev.spring.cloud.app.client.service.impl;

import static org.springframework.http.MediaType.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.feriodev.spring.cloud.app.client.model.Item;
import com.feriodev.spring.cloud.app.client.model.Product;
import com.feriodev.spring.cloud.app.client.service.ItemService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("serviceItem")
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private WebClient.Builder client;
	
	@Override
	public Flux<Item> findAll() {
		return client.build().get().uri("/list")
				.accept(APPLICATION_JSON)
				.exchangeToFlux(response -> response.bodyToFlux(Product.class))
				.map(p -> new Item(p, 1));
	}
	
	@Override
	public Mono<Item> findById(String id, Integer count) {
		Map<String, String> values = new HashMap<String, String>();
		values.put("id", id);
		return client.build().get().uri("/detail/{id}", values)
				.accept(APPLICATION_JSON)
				.retrieve()
				.bodyToMono(Product.class)
				.map(p -> new Item(p, count));
	}

}
