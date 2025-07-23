package com.vanessa.starwarsplanetsapi.planet.service;

import com.vanessa.starwarsplanetsapi.planet.domain.Planet;
import com.vanessa.starwarsplanetsapi.planet.domain.QueryBuilder;
import com.vanessa.starwarsplanetsapi.planet.repository.PlanetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlanetService {
    private final PlanetRepository repository;

    public Planet create(Planet planet){
        return repository.save(planet);
    }

    public Optional<Planet> get(Long id){ return repository.findById(id); }

    public Optional<Planet> getByName(String name){ return repository.findByName(name); }

    public Optional<Planet> getByClimate(String climate){ return repository.findByClimate(climate); }

    public List<Planet> list(String name, String climate, String terrain){
        Example<Planet> query = QueryBuilder.makeQuery(new Planet(name, climate, terrain));
        return repository.findAll(query);
    }
}
