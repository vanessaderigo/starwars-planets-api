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

    @Bean
    public Counter failedPlanetCreationsCounter(MeterRegistry meterRegistry){
        return Counter.builder("failed_planet_creations_total")
                .description("Total number of failed planet creation attempts")
                .register(meterRegistry);
    }
}
