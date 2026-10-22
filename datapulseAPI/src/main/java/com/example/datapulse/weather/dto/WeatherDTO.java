package com.example.datapulse.weather.dto;

import lombok.Builder;
import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonProperty;

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
        @JsonProperty("isDay")
        private Boolean isDay;
    }
}