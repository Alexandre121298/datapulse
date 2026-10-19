package com.example.datapulse.weather.dto;

import lombok.Builder;
import lombok.Getter;

public class WeatherDTO {

    @Getter
    @Builder
    public static class WeatherOutput {

        private String time;
        private String timezone;
        private Double temperature;
        private Double apparentTemperature;
        private Integer humidity;
        private Double windSpeed;
        private Integer weatherCode;
    }
}