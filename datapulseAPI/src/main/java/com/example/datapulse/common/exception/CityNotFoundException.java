package com.example.datapulse.common.exception;

public class CityNotFoundException extends RuntimeException {

    public CityNotFoundException(String cityName, String country) {
        super("La ville " + cityName + " située dans le pays "
                + country + " est introuvable.");
    }
}
