package com.example.datapulse.weather.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public class TrackedCityDTO {


    @Data
    @AllArgsConstructor
    @Builder
    public static class TrackedCityInput
    {
        @NotNull @NotBlank
        String name;
        @NotNull @NotBlank
        String country;
        @NotNull @NotBlank @Email
        String userEmail;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class TrackedCityOutput
    {
        Integer id;
        String name;
        String country;
        Double latitude;
        Double longitude;
    }
}
