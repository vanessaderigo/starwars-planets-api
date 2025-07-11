package com.vanessa.starwarsplanetsapi.planet;

import com.vanessa.starwarsplanetsapi.planet.domain.Planet;
import com.vanessa.starwarsplanetsapi.planet.repository.PlanetRepository;
import com.vanessa.starwarsplanetsapi.planet.service.PlanetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.vanessa.starwarsplanetsapi.commom.PlanetConstants.INVALID_PLANET;
import static com.vanessa.starwarsplanetsapi.commom.PlanetConstants.PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {
    @InjectMocks
    private PlanetService service;

    @Mock
    private PlanetRepository repository;

    @Test
    public void createPlanet_WithValidData_ReturnsPlanet(){
        when(repository.save(PLANET)).thenReturn(PLANET);
        Planet sut = service.create(PLANET);
        assertThat(sut).isEqualTo(PLANET);
    }

    @Test
    public void createPlanet_WithInvalidData_ThrowsException(){
        when(repository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);
        assertThatThrownBy( () -> service.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }
}
