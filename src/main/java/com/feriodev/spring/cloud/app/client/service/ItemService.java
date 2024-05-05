package com.feriodev.spring.cloud.app.client.service;

import com.feriodev.spring.cloud.app.client.model.Item;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemService {

	public Flux<Item> findAll();

	public Mono<Item> findById(String id, Integer count);
}
