package com.vanessa.starwarsplanetsapi.metrics;

import com.vanessa.starwarsplanetsapi.repository.PlanetRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@RequiredArgsConstructor
public class MetricsConfiguration {
    private final PlanetRepository repository;
    private final AtomicInteger activePlanets = new AtomicInteger(0);

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

    @Bean
    public Gauge activePlanetsGauge(MeterRegistry meterRegistry){
        activePlanets.set((int) repository.count());

        return Gauge.builder("active_planets", activePlanets, AtomicInteger::get)
                .description("Number of planets currently in database")
                .register(meterRegistry);
    }

    public void incrementActivePlanets(){
        activePlanets.incrementAndGet();
    }

    public void decrementActivePlanets(){
        activePlanets.decrementAndGet();
    }

    @Bean
    public Timer planetCreationTimer(MeterRegistry meterRegistry){
        return Timer.builder("planet_creation_duration")
                .description("Time taken to create a planet")
                .register(meterRegistry);
    }
}
