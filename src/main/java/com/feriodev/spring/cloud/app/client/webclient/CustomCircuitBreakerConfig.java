package com.feriodev.spring.cloud.app.client.webclient;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@Configuration
public class CustomCircuitBreakerConfig {

	@Bean
	Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault(id -> {
			return new Resilience4JConfigBuilder(id)
				.circuitBreakerConfig(CircuitBreakerConfig.custom()
						.slidingWindowSize(10) //umbral de request
						.failureRateThreshold(50) //% de fallas para abrir circuito
						.waitDurationInOpenState(Duration.ofSeconds(10L)) //seg. duracion de espera para semi-abierto
						.permittedNumberOfCallsInHalfOpenState(5) //nro de request en semi-abierto
						.slowCallRateThreshold(50) //% de llamadas lentas
						.slowCallDurationThreshold(Duration.ofSeconds(2L)) //seg. duracion llamadas lentas
						.build())
				.timeLimiterConfig( //Tiempo de timeout, se ejecuta primero que el valor de llamadas lentas
						TimeLimiterConfig.custom()
							.timeoutDuration(Duration.ofSeconds(2L))
							.build())
				.build();
		});
	}
}
