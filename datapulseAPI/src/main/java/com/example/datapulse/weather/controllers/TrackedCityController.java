package com.example.datapulse.weather.controllers;

import com.example.datapulse.weather.dto.TrackedCityDTO;
import com.example.datapulse.weather.models.TrackedCityEntity;
import com.example.datapulse.weather.services.TrackedCityServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tracked-cities")
public class TrackedCityController {

    private final TrackedCityServices trackedCityService;

    public TrackedCityController(TrackedCityServices trackedCityService) {
        this.trackedCityService = trackedCityService;
    }

    @Operation(summary = "Lister les villes du compte connecté")
    @GetMapping("/all-tracked")
    public ResponseEntity<Page<TrackedCityDTO.TrackedCityOutput>>
    getAllTrackedCitiesByUser(
            @AuthenticationPrincipal UserDetails user,
            Pageable pageable
    ) throws BadRequestException {

        return ResponseEntity.ok(
                trackedCityService.getAllTrackedCitiesByUser(
                        user.getUsername(),
                        pageable
                )
        );
    }

    @Operation(summary = "Retourner une ville du compte connecté")
    @GetMapping("/city")
    public ResponseEntity<TrackedCityDTO.TrackedCityOutput>
    getTrackedCityByUser(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam String cityName,
            @RequestParam String cityCountry
    ) throws BadRequestException {

        return ResponseEntity.ok(
                trackedCityService.getTrackedCityByUser(
                        user.getUsername(),
                        cityName,
                        cityCountry
                )
        );
    }

    @Operation(summary = "Ajouter une ville au compte connecté")
    @ApiResponse(responseCode = "201", description = "Ville suivie créée")
    @PostMapping("/create-tracked-city")
    public ResponseEntity<TrackedCityDTO.TrackedCityOutput>
    createTrackedCity(
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody TrackedCityDTO.TrackedCityInput input
    ) throws BadRequestException {

        TrackedCityEntity city = trackedCityService.createTrackedCity(
                input.getName(),
                input.getCountry(),
                user.getUsername()
        );

        TrackedCityDTO.TrackedCityOutput output =
                new TrackedCityDTO.TrackedCityOutput(
                        city.getId(),
                        city.getCityName(),
                        city.getCityCountry(),
                        city.getLatitude(),
                        city.getLongitude()
                );

        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @Operation(summary = "Supprimer une ville du compte connecté")
    @ApiResponse(responseCode = "204", description = "Ville supprimée")
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteTrackedCity(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam String cityName,
            @RequestParam String cityCountry
    ) throws BadRequestException {

        trackedCityService.deleteTrackedCity(
                user.getUsername(),
                cityName,
                cityCountry
        );

        return ResponseEntity.noContent().build();
    }
}