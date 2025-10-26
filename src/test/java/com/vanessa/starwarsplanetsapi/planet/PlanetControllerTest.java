package com.vanessa.starwarsplanetsapi.planet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vanessa.starwarsplanetsapi.domain.Planet;
import com.vanessa.starwarsplanetsapi.service.PlanetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.vanessa.starwarsplanetsapi.commom.PlanetConstants.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PlanetControllerTest {
    @MockitoBean
    private PlanetService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createPlanet_WithValidData_ReturnsCreated() throws Exception{
        when(service.create(PLANET)).thenReturn(PLANET);
        mockMvc.perform(post("/planets")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PLANET)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    public void createPlanet_WithInvalidData_ReturnsBadRequest() throws Exception {
        Planet emptyPlanet = new Planet();

        mockMvc.perform(post("/planets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyPlanet)))
                .andExpect(status().isUnprocessableEntity());

        mockMvc.perform(post("/planets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(INVALID_PLANET)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createPlanet_WithExistingName_ReturnsConflict() throws Exception{
        when(service.create(any())).thenThrow(DataIntegrityViolationException.class);
        mockMvc.perform(post("/planets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PLANET)))
                .andExpect(status().isConflict());
    }

    @Test
    public void getPlanet_ByExistingID_ReturnsPlanet() throws Exception {
        when(service.get(1L)).thenReturn(Optional.of(PLANET));
        mockMvc.perform(get("/planets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    public void getPlanet_ByUnexistingID_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/planets/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet() throws Exception {
        when(service.getByName(PLANET.getName())).thenReturn(Optional.of(PLANET));

        mockMvc.perform(get("/planets/name/" + PLANET.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    public void getPlanet_ByUnexistingName_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/planets/name/" + INVALID_PLANET.getName()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void listPlanets_ReturnsFilteredPlanets() throws Exception{
        when(service.list(null, null, null)).thenReturn(PLANETS);
        when(service.list(TATOOINE.getName(), TATOOINE.getClimate(), TATOOINE.getTerrain())).thenReturn(List.of(TATOOINE));

        mockMvc.perform(get("/planets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        mockMvc.perform(get("/planets?" + String.format("name=%s&terrain=%s&climate=%s", TATOOINE.getName(), TATOOINE.getTerrain(), TATOOINE.getClimate())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]").value(TATOOINE));
    }

    @Test
    public void listPlanets_ReturnsNoPlanets() throws Exception {
        when(service.list(null, null, null)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/planets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void removePlanet_ByExistingId_ReturnsNoContent() throws Exception{
        mockMvc.perform(delete("/planets/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void removePlanet_ByUnexistingId_ReturnsNotFound() throws Exception{
        final long planetId = 1L;
        doThrow(new EmptyResultDataAccessException(1)).when(service).delete(planetId);
        mockMvc.perform(delete("/planets/" + planetId))
                .andExpect(status().isNotFound());
    }
}
