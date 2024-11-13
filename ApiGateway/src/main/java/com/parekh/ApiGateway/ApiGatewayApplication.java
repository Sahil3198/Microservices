package com.parekh.ApiGateway;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

@SpringBootApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
				.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
				.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(2)).build())
				.build());
	}

//	@Bean
//	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
//		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
//				.circuitBreakerConfig(CircuitBreakerConfig.custom()
//						.failureRateThreshold(50)   // Open circuit on 50% failure rate
//						.waitDurationInOpenState(Duration.ofMillis(5000))  // Wait 5 seconds before retrying
//						.build())
//				.timeLimiterConfig(TimeLimiterConfig.custom()
//						.timeoutDuration(Duration.ofMillis(2000))  // 2-second timeout
//						.build())
//				.build());
//	}
}
