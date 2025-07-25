package com.vanessa.starwarsplanetsapi.planet;

import com.vanessa.starwarsplanetsapi.planet.domain.Planet;
import com.vanessa.starwarsplanetsapi.planet.repository.PlanetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static com.vanessa.starwarsplanetsapi.commom.PlanetConstants.INVALID_PLANET;
import static com.vanessa.starwarsplanetsapi.commom.PlanetConstants.PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class PlanetRepositoryTest {
    @Autowired
    PlanetRepository repository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    public void repositorySave_WithValidData_ReturnsPlanet(){
        Planet planet = repository.save(PLANET);
        Planet sut = testEntityManager.find(Planet.class, planet.getId());
        assertThat(sut).isNotNull();
        assertThat(sut.getName()).isEqualTo(planet.getName());
        assertThat(sut.getClimate()).isEqualTo(planet.getClimate());
        assertThat(sut.getTerrain()).isEqualTo(planet.getTerrain());
    }

    @Test
    public void repositorySave_WithInvalidData_ThrowsException(){
        Planet emptyPlanet = new Planet();
        assertThatThrownBy(() -> repository.save(emptyPlanet)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> repository.save(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void savePlanet_WithExistingName_ThrowsException(){
        Planet planet = testEntityManager.persistFlushFind(PLANET);
        testEntityManager.detach(planet);
        planet.setId(null);
        assertThatThrownBy(() -> repository.save(planet)).isInstanceOf(RuntimeException.class);
    }
}