package com.example.datapulse.weather.controllers;

import com.example.datapulse.weather.dto.WeatherDTO;
import com.example.datapulse.weather.services.WeatherServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherServices weatherServices;

    public WeatherController(WeatherServices weatherServices) {
        this.weatherServices = weatherServices;
    }

    @GetMapping("/current")
    public WeatherDTO.WeatherOutput getCurrentWeather(
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude
    ) {
        return weatherServices.getCurrentWeather(
                latitude,
                longitude
        );
    }
}