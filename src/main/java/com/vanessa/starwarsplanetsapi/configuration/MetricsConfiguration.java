package com.vanessa.starwarsplanetsapi.configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfiguration {
    @Bean
    public Counter createdPlanetsCounter(MeterRegistry meterRegistry){
        return Counter.builder("created_planets_total")
                .description("Total number of successfully created planets")
                .register(meterRegistry);
    }
}
