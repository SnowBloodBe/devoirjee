package com.mcommandes.config;

import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ResilienceConfig {

    @Bean
    public TimeLimiter produitServiceTimeLimiter() {
        return TimeLimiter.of(TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(2))
                .build());
    }
}
