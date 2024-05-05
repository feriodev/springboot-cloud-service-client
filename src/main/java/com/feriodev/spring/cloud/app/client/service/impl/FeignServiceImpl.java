package com.feriodev.spring.cloud.app.client.service.impl;

import org.springframework.stereotype.Service;

@Service("serviceFeign")
public class FeignServiceImpl /*implements ItemService*/ {

	/*@Autowired
	private FeignWebClient client;
	
	@Override
	public Flux<Item> findAll() {
		return client.list().map(p -> new Item(p, 1));
	}

	@Override
	public Mono<Item> findById(String id, Integer count) {
		return client.detail(id).map(p -> new Item(p, count));
	}*/

}
