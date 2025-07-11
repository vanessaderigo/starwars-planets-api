package com.vanessa.starwarsplanetsapi.planet.service;

import com.vanessa.starwarsplanetsapi.planet.domain.Planet;
import com.vanessa.starwarsplanetsapi.planet.repository.PlanetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanetService {
    private final PlanetRepository repository;

    public Planet create(Planet planet){
        return repository.save(planet);
    }
}
