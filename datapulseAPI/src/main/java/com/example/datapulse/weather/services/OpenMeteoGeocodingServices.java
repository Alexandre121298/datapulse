package com.example.datapulse.weather.services;

import com.example.datapulse.common.exception.CityNotFoundException;
import com.example.datapulse.common.exception.TrackedCityNotFoundException;
import com.example.datapulse.weather.client.OpenMeteoGeocodingClient;
import com.example.datapulse.weather.dto.OpenMeteoGeocodingDTO;
import org.springframework.stereotype.Service;

@Service
public class OpenMeteoGeocodingServices {

    private final OpenMeteoGeocodingClient geocodingClient;

    public OpenMeteoGeocodingServices(
            OpenMeteoGeocodingClient geocodingClient
    ) {
        this.geocodingClient = geocodingClient;
    }

    public OpenMeteoGeocodingDTO.OpenMeteoGeocodingResult findCity(
            String cityName,
            String cityCountry
    ) {
        OpenMeteoGeocodingDTO.OpenMeteoGeocodingResponse response =
                geocodingClient.searchCity(cityName);

        if (response == null || response.getResults() == null) {
            throw new CityNotFoundException(cityName,cityCountry);
        }

        return response.getResults()
                .stream()
                .filter(result -> result.getCountry() != null)
                .filter(result ->
                        result.getCountry().equalsIgnoreCase(cityCountry)
                )
                .findFirst()
                .orElseThrow(() ->
                        new CityNotFoundException(cityName,cityCountry)
                );
    }
}
