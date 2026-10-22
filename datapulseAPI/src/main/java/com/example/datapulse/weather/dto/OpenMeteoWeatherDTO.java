package com.example.datapulse.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class OpenMeteoWeatherDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OpenMeteoWeatherResponse {

        private String timezone;
        private CurrentWeather current;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CurrentWeather {

        private String time;

        @JsonProperty("temperature_2m")
        private Double temperature;

        @JsonProperty("apparent_temperature")
        private Double apparentTemperature;

        @JsonProperty("relative_humidity_2m")
        private Integer humidity;

        @JsonProperty("wind_speed_10m")
        private Double windSpeed;

        @JsonProperty("weather_code")
        private Integer weatherCode;

        @JsonProperty("is_day")
        private Integer isDay;
    }
}