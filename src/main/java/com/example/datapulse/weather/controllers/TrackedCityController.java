package com.example.datapulse.weather.controllers;

import com.example.datapulse.weather.dto.TrackedCityDTO;
import com.example.datapulse.weather.models.TrackedCityEntity;
import com.example.datapulse.weather.services.TrackedCityServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tracked-cities")
public class TrackedCityController {

    private final TrackedCityServices trackedCityService;

    public TrackedCityController(TrackedCityServices trackedCityService) {
        this.trackedCityService = trackedCityService;
    }

    @Operation(summary = "Lister la totalité des villes suivies par l'utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Toutes les villes sont retournées"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
    })
    @GetMapping("/all-tracked")
    public ResponseEntity<Page<TrackedCityDTO.TrackedCityOutput>> getAllTrackedCitiesByUser(@RequestParam String userEmail, Pageable pageable) throws BadRequestException {

        log.info("L'Email utilisateur reçu : "+ userEmail);

        return ResponseEntity.ok(trackedCityService.getAllTrackedCitiesByUser(userEmail,pageable));
    }

    @Operation(summary = "Retourner une ville spécifique suivie par l'utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "La ville demandée est retournée"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
    })
    @GetMapping("city")
    public ResponseEntity<TrackedCityDTO.TrackedCityOutput> getTrackedCityByUser(
            @Valid @RequestParam String userEmail,
            @Valid @RequestParam String cityName,
            @Valid @RequestParam String cityCountry) throws BadRequestException {
        log.info("L'Email utilisateur reçu :"+userEmail);
        log.info("Le nom de la ville demandée et suivi par l'utilisateur :"+cityName);
        log.info("Le pays de la ville suivie demandée par l'utilisateur :"+cityCountry);

        return ResponseEntity.ok(trackedCityService.getTrackedCityByUser(userEmail,cityName,cityCountry));
    }

    @Operation(summary = "Créer une ville suivie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ville suivie créée"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "409", description = "Ville déjà suivie")
    })
    @PostMapping("/create-tracked-city")
    public TrackedCityDTO.TrackedCityOutput createTrackedCity(@Valid @RequestBody TrackedCityDTO.TrackedCityInput input) throws BadRequestException {

        log.info("L'Email utilisateur reçu :"+input.getUserEmail());
        log.info("Le nom de la ville demandée et suivi par l'utilisateur :"+input.getName());
        log.info("Le pays de la ville suivie demandée par l'utilisateur :"+input.getCountry());

        TrackedCityEntity trackedCityEntity = trackedCityService.createTrackedCity(input.getName(),input.getCountry(),input.getUserEmail());


        return TrackedCityDTO.TrackedCityOutput.builder()
                .id(trackedCityEntity.getId())
                .name(trackedCityEntity.getCityName())
                .country(trackedCityEntity.getCityCountry())
                .build();
    }

    @Operation(summary = "Supprimer une ville suivie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "La ville a été supprimé"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
    })
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteTrackedCity(
              @Valid @RequestParam String userEmail,
              @Valid @RequestParam String cityName,
              @Valid @RequestParam String cityCountry) throws BadRequestException {

        log.info("L'Email utilisateur reçu :"+userEmail);
        log.info("Le nom de la ville demandée et suivi par l'utilisateur :"+cityName);
        log.info("Le pays de la ville suivie demandée par l'utilisateur :"+cityCountry);

        trackedCityService.deleteTrackedCity(userEmail,cityName,cityCountry);

        log.info("La ville a bien été supprimé !");

        return ResponseEntity.ok().build();
    }


}
