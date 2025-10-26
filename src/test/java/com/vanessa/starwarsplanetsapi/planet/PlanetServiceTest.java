package com.vanessa.starwarsplanetsapi.planet;

import com.vanessa.starwarsplanetsapi.domain.Planet;
import com.vanessa.starwarsplanetsapi.domain.QueryBuilder;
import com.vanessa.starwarsplanetsapi.metrics.MetricsConfiguration;
import com.vanessa.starwarsplanetsapi.repository.PlanetRepository;
import com.vanessa.starwarsplanetsapi.service.PlanetService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static com.vanessa.starwarsplanetsapi.commom.PlanetConstants.INVALID_PLANET;
import static com.vanessa.starwarsplanetsapi.commom.PlanetConstants.PLANET;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {
    @InjectMocks
    private PlanetService service;

    @Mock
    private PlanetRepository repository;

    @Mock
    private MetricsConfiguration metrics;

    @Mock
    private Counter createdPlanetsCounter;

    @Mock
    private Timer planetCreationTimer;

    @BeforeEach
    void setupMocks() {
        lenient().when(planetCreationTimer.record(any(Supplier.class)))
                .thenAnswer(invocation -> {
                    Supplier<?> supplier = invocation.getArgument(0);
                    return supplier.get();
                });
    }

    @Test
    public void createPlanet_WithValidData_ReturnsPlanet(){
        when(repository.save(PLANET)).thenReturn(PLANET);

        Planet sut = service.create(PLANET);

        assertThat(sut).isEqualTo(PLANET);
        verify(repository).save(PLANET);
        verify(createdPlanetsCounter).increment();
        verify(metrics).incrementActivePlanets();
    }

    @Test
    public void createPlanet_WithInvalidData_ThrowsException(){
        when(repository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);

        assertThatThrownBy( () -> service.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);

        verify(repository).save(INVALID_PLANET);
        verify(createdPlanetsCounter, never()).increment();
        verify(metrics, never()).incrementActivePlanets();
    }

    @Test
    public void getPlanet_ByExistingId_ReturnsPlanet(){
        when(repository.findById(PLANET.getId())).thenReturn(Optional.of(PLANET));
        Optional<Planet> sut = service.get(PLANET.getId());
        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanet_ByNonexistentId_ReturnsNoPlanet(){
        when(repository.findById(1L)).thenReturn(Optional.empty());
        Optional<Planet> sut = service.get(1L);
        assertThat(sut).isEmpty();
    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet(){
        when(repository.findByName(PLANET.getName())).thenReturn(Optional.of(PLANET));
        Optional<Planet> sut = service.getByName(PLANET.getName());
        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanet_ByNonexistentName_ReturnsNoPlanet(){
        final String name = "Nonexistent name";

        when(repository.findByName(name)).thenReturn(Optional.empty());
        Optional<Planet> sut = service.getByName(name);
        assertThat(sut).isEmpty();
    }

    @Test
    public void listPlanets_ReturnsAllPlanets(){
        List<Planet> planets = new ArrayList<>() {{
            add(PLANET);
        }};
        Example<Planet> query = QueryBuilder.makeQuery(new Planet(PLANET.getName(), PLANET.getClimate(), PLANET.getTerrain()));
        when(repository.findAll(query)).thenReturn(planets);
        List<Planet> sut = service.list(PLANET.getName(), PLANET.getClimate(), PLANET.getTerrain());
        assertThat(sut).isNotEmpty();
        assertThat(sut).hasSize(1);
        assertThat(sut.getFirst()).isEqualTo(PLANET);
    }

    @Test
    public void listPlanets_ReturnsNoPlanets(){
        when(repository.findAll(ArgumentMatchers.<Example<Planet>>any())).thenReturn(Collections.emptyList());
        List<Planet> sut = service.list(PLANET.getName(), PLANET.getClimate(), PLANET.getTerrain());
        assertThat(sut).isEmpty();
    }

    @Test
    public void deletePlanet_ByExistingId_DoesNotThrowAnyException(){
        assertThatCode(() -> service.delete(1L)).doesNotThrowAnyException();
    }
}
