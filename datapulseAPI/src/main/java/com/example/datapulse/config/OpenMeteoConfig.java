package com.example.datapulse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class OpenMeteoConfig {

    @Bean
    public RestClient openMeteoGeocodingRestClient(
            RestClient.Builder restClientBuilder
    ) {
        return restClientBuilder
                .baseUrl("https://geocoding-api.open-meteo.com")
                .build();
    }
}