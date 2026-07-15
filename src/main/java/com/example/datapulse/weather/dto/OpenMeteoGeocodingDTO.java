package com.example.datapulse.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class OpenMeteoGeocodingDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OpenMeteoGeocodingResponse {

        private List<OpenMeteoGeocodingResult> results;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OpenMeteoGeocodingResult {

        private Integer id;
        private String name;
        private Double latitude;
        private Double longitude;
        private String country;
    }
}
