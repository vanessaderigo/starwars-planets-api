package com.vanessa.starwarsplanetsapi.planet.controller;

import com.vanessa.starwarsplanetsapi.planet.domain.Planet;
import com.vanessa.starwarsplanetsapi.planet.service.PlanetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/planets")
@RequiredArgsConstructor
public class PlanetController {
    private final PlanetService service;

    @PostMapping
    public ResponseEntity<Planet> create(@RequestBody Planet planet){
        service.create(planet);
        return ResponseEntity.status(HttpStatus.CREATED).body(planet);
    }
}
