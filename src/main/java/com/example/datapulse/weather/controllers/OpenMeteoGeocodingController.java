package com.example.datapulse.weather.controllers;

import com.example.datapulse.weather.dto.OpenMeteoGeocodingDTO;
import com.example.datapulse.weather.services.OpenMeteoGeocodingServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/geocoding")
public class OpenMeteoGeocodingController {

    private final OpenMeteoGeocodingServices geocodingServices;

    public OpenMeteoGeocodingController(
            OpenMeteoGeocodingServices geocodingServices
    ) {
        this.geocodingServices = geocodingServices;
    }

    @GetMapping
    public OpenMeteoGeocodingDTO.OpenMeteoGeocodingResult findCity(
            @RequestParam String cityName,
            @RequestParam String country
    ) {
        return geocodingServices.findCity(cityName, country);
    }

}
