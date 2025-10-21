package com.vanessa.starwarsplanetsapi.service;

import com.vanessa.starwarsplanetsapi.metrics.MetricsConfiguration;
import com.vanessa.starwarsplanetsapi.domain.Planet;
import com.vanessa.starwarsplanetsapi.domain.QueryBuilder;
import com.vanessa.starwarsplanetsapi.repository.PlanetRepository;
import io.micrometer.core.instrument.Counter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlanetService {
    private final PlanetRepository repository;
    private final Counter createdPlanetsCounter;
    private final MetricsConfiguration metrics;

    public Planet create(Planet planet){
        Planet saved = repository.save(planet);
        createdPlanetsCounter.increment();
        metrics.incrementActivePlanets();
        return saved;
    }

    public Optional<Planet> get(Long id){ return repository.findById(id); }

    public Optional<Planet> getByName(String name){ return repository.findByName(name); }

    public List<Planet> list(String name, String climate, String terrain){
        Example<Planet> query = QueryBuilder.makeQuery(new Planet(name, climate, terrain));
        return repository.findAll(query);
    }

    public void delete(Long id){
        if (repository.existsById(id)){
            repository.deleteById(id);
            metrics.decrementActivePlanets();
        }
    }
}
