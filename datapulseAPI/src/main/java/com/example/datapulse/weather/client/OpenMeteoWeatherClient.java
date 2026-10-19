package com.example.datapulse.weather.client;

import com.example.datapulse.weather.dto.OpenMeteoWeatherDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OpenMeteoWeatherClient {

    private final RestClient restClient;

    public OpenMeteoWeatherClient(
            @Qualifier("openMeteoWeatherRestClient") RestClient restClient
    ) {
        this.restClient = restClient;
    }

    public OpenMeteoWeatherDTO.OpenMeteoWeatherResponse getCurrentWeather(
            double latitude,
            double longitude
    ) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/forecast")
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam(
                                "current",
                                "temperature_2m,apparent_temperature,"
                                        + "relative_humidity_2m,"
                                        + "wind_speed_10m,weather_code"
                        )
                        .queryParam("temperature_unit", "celsius")
                        .queryParam("wind_speed_unit", "kmh")
                        .queryParam("timezone", "auto")
                        .build())
                .retrieve()
                .body(
                        OpenMeteoWeatherDTO.OpenMeteoWeatherResponse.class
                );
    }
}