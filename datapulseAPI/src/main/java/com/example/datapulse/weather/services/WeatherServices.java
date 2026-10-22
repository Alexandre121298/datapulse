package com.example.datapulse.weather.services;

import com.example.datapulse.weather.client.OpenMeteoWeatherClient;
import com.example.datapulse.weather.dto.OpenMeteoWeatherDTO;
import com.example.datapulse.weather.dto.WeatherDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

@Service
public class WeatherServices {

    private final OpenMeteoWeatherClient weatherClient;

    public WeatherServices(OpenMeteoWeatherClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    public WeatherDTO.WeatherOutput getCurrentWeather(
            double latitude,
            double longitude
    ) {
        if (!Double.isFinite(latitude)
                || latitude < -90
                || latitude > 90) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La latitude doit être comprise entre -90 et 90."
            );
        }

        if (!Double.isFinite(longitude)
                || longitude < -180
                || longitude > 180) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La longitude doit être comprise entre -180 et 180."
            );
        }

        OpenMeteoWeatherDTO.OpenMeteoWeatherResponse response;

        try {
            response = weatherClient.getCurrentWeather(
                    latitude,
                    longitude
            );
        } catch (RestClientException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Impossible de récupérer la météo auprès d'Open-Meteo.",
                    exception
            );
        }

        if (response == null || response.getCurrent() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Open-Meteo n'a renvoyé aucune météo actuelle."
            );
        }

        OpenMeteoWeatherDTO.CurrentWeather current =
                response.getCurrent();

        return WeatherDTO.WeatherOutput.builder()
                .time(current.getTime())
                .timezone(response.getTimezone())
                .temperature(current.getTemperature())
                .apparentTemperature(current.getApparentTemperature())
                .humidity(current.getHumidity())
                .windSpeed(current.getWindSpeed())
                .weatherCode(current.getWeatherCode())
                .isDay(
                        current.getIsDay() == null
                                ? null
                                : current.getIsDay() == 1
                )
                .build();
    }
}