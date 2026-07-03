package com.example.datapulse.weather.controllers;

import com.example.datapulse.weather.dto.TrackedCityDTO;
import com.example.datapulse.weather.models.TrackedCityEntity;
import com.example.datapulse.weather.services.TrackedCityServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tracked-cities")
public class TrackedCityController {

    private final TrackedCityServices trackedCityService;

    public TrackedCityController(TrackedCityServices trackedCityService) {
        this.trackedCityService = trackedCityService;
    }

    @GetMapping("/all-tracked")
    public ResponseEntity<List<TrackedCityDTO.TrackedCityOutput>> getAllTrackedCitiesByUser(@RequestParam String userEmail) {

        log.info("L'Email utilisateur reçu : "+ userEmail);

        return ResponseEntity.ok(trackedCityService.getAllTrackedCitiesByUser(userEmail));
    }

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

    @PostMapping("/create-tracked-city")
    public TrackedCityDTO.TrackedCityOutput createTrackedCity(@Valid @RequestBody TrackedCityDTO.TrackedCityInput input) {


        TrackedCityEntity trackedCityEntity = trackedCityService.createTrackedCity(input.getName(),input.getCountry(),input.getUserEmail());


        return TrackedCityDTO.TrackedCityOutput.builder()
                .id(trackedCityEntity.getId())
                .name(trackedCityEntity.getCityName())
                .country(trackedCityEntity.getCityCountry())
                .build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrackedCity(@PathVariable Integer id) {
        trackedCityService.deleteTrackedCity(id);

        log.info("La ville a bien été supprimé !");

        return ResponseEntity.ok().build();
    }


}
