package com.example.datapulse.weather.client;

import com.example.datapulse.weather.dto.OpenMeteoGeocodingDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class OpenMeteoGeocodingClient {

    private final RestClient restClient;

    public OpenMeteoGeocodingClient(
            @Qualifier("openMeteoGeocodingRestClient") RestClient restClient
    ) {
        this.restClient = restClient;
    }

    public OpenMeteoGeocodingDTO.OpenMeteoGeocodingResponse searchCity(String cityName) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/search")
                        .queryParam("name", cityName)
                        .queryParam("count", 10)
                        .queryParam("language", "fr")
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .body(OpenMeteoGeocodingDTO.OpenMeteoGeocodingResponse.class);
    }
}