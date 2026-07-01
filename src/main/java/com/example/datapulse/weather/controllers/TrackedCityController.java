package com.example.datapulse.weather.controllers;

import com.example.datapulse.weather.dto.TrackedCityDTO;
import com.example.datapulse.weather.models.TrackedCityEntity;
import com.example.datapulse.weather.services.TrackedCityServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/tracked-cities")
@RequiredArgsConstructor
public class TrackedCityController {

    private final TrackedCityServices trackedCityService;

    @GetMapping("/all-tracked/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TrackedCityDTO.TrackedCityOutput> getTrackedCity(@PathVariable Integer userId) {

        log.info("L'id reçu : "+ userId);

        //List<TrackedCityEntity> userTrackedCityList = TrackedCityServices.getAllTrackedCityByUser(1);

        return null;
    }

    @PostMapping("/create-tracked-city")
    public TrackedCityDTO.TrackedCityOutput createTrackedCity(@Valid @RequestBody TrackedCityDTO.TrackedCityInput input) {


        TrackedCityEntity trackedCityEntity = trackedCityService.createTrackedCity(input.getName(),input.getCountry(),input.getUserEmail());


        return TrackedCityDTO.TrackedCityOutput.builder()
                .id(trackedCityEntity.getId())
                .name(trackedCityEntity.getName())
                .country(trackedCityEntity.getCountry())
                .build();

    }

    public void deleteTrackedCity()
    {
        //todo : Implementer le delete
        //Objectif : Pouvoir supprimer une ville qui est suivie par l'utilisateur
    }


}
