package com.vanessa.starwarsplanetsapi;

import com.vanessa.starwarsplanetsapi.planet.domain.Planet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static com.vanessa.starwarsplanetsapi.commom.PlanetConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/import_planets.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/remove_planets.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PlanetIT {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createPlanet_WithValidData_ReturnsCreated(){
        ResponseEntity<Planet> sut = restTemplate.postForEntity("/planets", PLANET, Planet.class);
        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(sut.getBody().getId()).isNotNull();
        assertThat(sut.getBody().getName()).isEqualTo(PLANET.getName());
        assertThat(sut.getBody().getClimate()).isEqualTo(PLANET.getClimate());
        assertThat(sut.getBody().getTerrain()).isEqualTo(PLANET.getTerrain());
    }

    @Test
    public void createPlanet_WithInvalidData_ReturnsUnprocessableEntity() {
        ResponseEntity<Planet> sut = restTemplate.postForEntity("/planets", INVALID_PLANET, Planet.class);
        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void getPlanet_WithExistingId_ReturnsPlanet(){
        ResponseEntity<Planet> sut = restTemplate.getForEntity("/planets/1", Planet.class);
        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).isEqualTo(TATOOINE);
    }

    @Test
    public void getPlanet_WithUnexistingId_ReturnsNotFound() {
        ResponseEntity<Planet> sut = restTemplate.getForEntity("/planets/99", Planet.class);
        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(sut.getBody()).isNull();
    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet() {
        ResponseEntity<Planet> sut = restTemplate.getForEntity("/planets/name/" + TATOOINE.getName(), Planet.class);
        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).isEqualTo(TATOOINE);
    }

    @Test
    public void getPlanet_ByUnexistingName_ReturnsNotFound() {
        ResponseEntity<Planet> sut = restTemplate.getForEntity("/planets/name/nonExistingName", Planet.class);
        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(sut.getBody()).isNull();
    }
}
