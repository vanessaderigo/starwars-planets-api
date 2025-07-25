package com.vanessa.starwarsplanetsapi.planet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vanessa.starwarsplanetsapi.planet.controller.PlanetController;
import com.vanessa.starwarsplanetsapi.planet.domain.Planet;
import com.vanessa.starwarsplanetsapi.planet.service.PlanetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.vanessa.starwarsplanetsapi.commom.PlanetConstants.INVALID_PLANET;
import static com.vanessa.starwarsplanetsapi.commom.PlanetConstants.PLANET;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlanetController.class)
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
}
