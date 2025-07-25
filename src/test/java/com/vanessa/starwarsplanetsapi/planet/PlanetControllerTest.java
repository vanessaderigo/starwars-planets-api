package com.vanessa.starwarsplanetsapi.planet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vanessa.starwarsplanetsapi.planet.controller.PlanetController;
import com.vanessa.starwarsplanetsapi.planet.service.PlanetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.vanessa.starwarsplanetsapi.commom.PlanetConstants.PLANET;
import static org.mockito.Mockito.when;
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
}
