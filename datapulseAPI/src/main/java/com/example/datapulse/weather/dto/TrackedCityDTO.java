package com.example.datapulse.weather.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TrackedCityDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TrackedCityInput {

        @NotBlank(message = "Le nom de la ville est obligatoire.")
        private String name;

        @NotBlank(message = "Le pays est obligatoire.")
        private String country;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class TrackedCityOutput {

        private Integer id;
        private String name;
        private String country;
        private Double latitude;
        private Double longitude;
    }
}