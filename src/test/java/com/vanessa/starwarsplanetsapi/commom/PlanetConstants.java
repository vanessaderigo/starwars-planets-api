package com.vanessa.starwarsplanetsapi.commom;

import com.vanessa.starwarsplanetsapi.planet.domain.Planet;

public class PlanetConstants {
    public static final Planet PLANET = new Planet("name", "climate", "terrain");
    public static final Planet INVALID_PLANET = new Planet("", "", "");
}
