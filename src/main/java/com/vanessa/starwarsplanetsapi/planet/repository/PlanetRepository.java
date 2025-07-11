package com.vanessa.starwarsplanetsapi.planet.repository;

import com.vanessa.starwarsplanetsapi.planet.domain.Planet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanetRepository extends JpaRepository<Planet, Long> {
}
