package com.vanessa.starwarsplanetsapi.planet;

import com.vanessa.starwarsplanetsapi.domain.Planet;
import com.vanessa.starwarsplanetsapi.domain.QueryBuilder;
import com.vanessa.starwarsplanetsapi.repository.PlanetRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Example;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.vanessa.starwarsplanetsapi.commom.PlanetConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class PlanetRepositoryTest {
    @Autowired
    private PlanetRepository repository;

    @Autowired
    TestEntityManager testEntityManager;

    @AfterEach
    public void afterEach(){
        PLANET.setId(null);
    }

    @Test
    public void repositorySave_WithValidData_ReturnsPlanet(){
        Planet planet = repository.save(PLANET);
        Planet sut = testEntityManager.find(Planet.class, planet.getId());
        assertThat(sut).isNotNull();
        assertThat(sut.getName()).isEqualTo(planet.getName());
        assertThat(sut.getClimate()).isEqualTo(planet.getClimate());
        assertThat(sut.getTerrain()).isEqualTo(planet.getTerrain());
    }

    @ParameterizedTest
    @MethodSource("providesInvalidPlanets")
    public void repositorySave_WithInvalidData_ThrowsException(Planet planet){
        assertThatThrownBy(() -> repository.save(planet)).isInstanceOf(RuntimeException.class);
    }

    private static Stream<Arguments> providesInvalidPlanets() {
        return Stream.of(
                Arguments.of(new Planet(null, "climate", "terrain")),
                Arguments.of(new Planet("name", null, "terrain")),
                Arguments.of(new Planet("name", "climate", null)),
                Arguments.of(new Planet(null,null, "terrain")),
                Arguments.of(new Planet(null,"climate", null)),
                Arguments.of(new Planet("name",null, null)),
                Arguments.of(new Planet(null,null, null)),
                Arguments.of(new Planet("","climate", "terrain")),
                Arguments.of(new Planet("name","", "terrain")),
                Arguments.of(new Planet("name","climate", "")),
                Arguments.of(new Planet("","", "terrain")),
                Arguments.of(new Planet("","climate", "")),
                Arguments.of(new Planet("name","", "")),
                Arguments.of(new Planet("","", ""))
        );
    }

    @Test
    public void savePlanet_WithExistingName_ThrowsException(){
        Planet planet = testEntityManager.persistFlushFind(PLANET);
        testEntityManager.detach(planet);
        planet.setId(null);
        assertThatThrownBy(() -> repository.save(planet)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getPlanet_ByExistingId_ReturnsPlanet(){
        Planet planet = testEntityManager.persistFlushFind(PLANET);
        Optional<Planet> optional = repository.findById(planet.getId());
        assertThat(optional).isNotEmpty();
        assertThat(optional.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanet_ByUnexistingId_ReturnsNoPlanet(){
        Optional<Planet> optional = repository.findById(1L);
        assertThat(optional).isEmpty();
    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet(){
        Planet planet = testEntityManager.persistFlushFind(PLANET);
        Optional<Planet> optional = repository.findByName(planet.getName());
        assertThat(optional).isPresent();
        assertThat(optional.get()).isEqualTo(planet);
    }

    @Test
    public void getPlanet_ByUnexistingName_ReturnsEmpty(){
        Optional<Planet> optional = repository.findByName("name");
        assertThat(optional).isEmpty();
    }

    @Test
    @Sql(scripts = {"/import_planets.sql"})
    public void listPlanets_ReturnsFilteredPlanets(){
        Example<Planet> queryWithoutFilters = QueryBuilder.makeQuery(new Planet());
        Example<Planet> queryWithFilters = QueryBuilder.makeQuery(new Planet(TATOOINE.getName(), TATOOINE.getClimate(), TATOOINE.getTerrain()));

        List<Planet> responseWithoutFilters = repository.findAll(queryWithoutFilters);
        List<Planet> responseWithFilters = repository.findAll(queryWithFilters);

        assertThat(responseWithoutFilters).isNotEmpty();
        assertThat(responseWithoutFilters).hasSize(3);

        assertThat(responseWithFilters).isNotEmpty();
        assertThat(responseWithFilters).hasSize(1);
        assertThat(responseWithFilters.getFirst()).isEqualTo(TATOOINE);
    }

    @Test
    public void listPlanets_ReturnsNoPlanets(){
        Example<Planet> query = QueryBuilder.makeQuery(new Planet());
        List<Planet> response = repository.findAll(query);
        assertThat(response).isEmpty();
    }

    @Test
    public void removePlanet_ByExistingId_RemovesPlanetFromDatabase(){
        Planet planet = testEntityManager.persistFlushFind(PLANET);
        repository.deleteById(planet.getId());
        Planet removedPlanet = testEntityManager.find(Planet.class, planet.getId());
        assertThat(removedPlanet).isNull();
    }
}