package com.feriodev.spring.cloud.app.client.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.feriodev.spring.cloud.app.client.model.Item;
import com.feriodev.spring.cloud.app.client.model.Product;
import com.feriodev.spring.cloud.app.client.service.ItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RefreshScope
@RestController
@RequestMapping
@Slf4j
public class ItemController {

	@Autowired
	@Qualifier("serviceItem")
	private ItemService service;
	
	@Value("${configuration.text}")
	private String configurationText;
	
	@Autowired
	private Environment env;
	
	@SuppressWarnings("rawtypes")
	@Autowired
	private ReactiveCircuitBreakerFactory cbFactory;

	@GetMapping("/list")
	public Mono<ResponseEntity<Flux<Item>>> list(
			@RequestParam(name = "name", required = false) String name,
			@RequestHeader(name = "token-request", required = false) String token) {
		
		log.info("name: " + name);
		log.info("token: " + token);
		
		return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(service.findAll()));
	}

	@GetMapping("/detail/{id}/count/{count}")
	public Mono<ResponseEntity<Item>> detail(@PathVariable String id, @PathVariable Integer count) {
		return service.findById(id, Integer.valueOf(count))
						.map(p -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(p))
						.defaultIfEmpty(ResponseEntity.notFound().build())
						.transform(t -> cbFactory.create("client").run(t, error -> detailAlternative(id, count, error)));
	}
	
	@CircuitBreaker(name = "client", fallbackMethod = "detailAlternative")
	@GetMapping("/detail2/{id}/count/{count}")
	public Mono<ResponseEntity<Item>> detail2(@PathVariable String id, @PathVariable Integer count) {
		return service.findById(id, Integer.valueOf(count))
						.map(p -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(p))
						.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	public Mono<ResponseEntity<Item>> detailAlternative(String id, Integer count, Throwable e) {
		log.error("Metodo detailAlternative");
		log.error(e.getMessage());
		
		Product product = Product.builder().id("123456").name("CircuitBreaker item").price(0.0).build();
		Item item = Item.builder().count(count).product(product).build();	
		return Mono.just(ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON).body(item))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	
	@GetMapping("/config")
	public ResponseEntity<Map<String, String>> getConfig(@Value("${server.port}") String port) {
		Map<String, String> json = new HashMap<>();
		json.put("config", configurationText);
		json.put("port", port);		
		
		log.info("Json: " + json.toString());
		
		if (env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("dev")) {
			json.put("author.name", env.getProperty("configuration.author.name"));
			json.put("author.email", env.getProperty("configuration.author.email"));
		}
		
		return ResponseEntity.ok().body(json);
	}
	
	@PostMapping("/create")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Product> save(@RequestBody Product product) {
		return service.save(product);
	}
	
	@PutMapping("/update/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Product> update(@PathVariable String id, @RequestBody Product product) {
		return service.update(id, product);
	}
	
	@DeleteMapping("/delete/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> delete(@PathVariable String id) {
		return service.delete(id);
	}
	
}