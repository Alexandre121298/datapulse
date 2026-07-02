package com.example.datapulse.weather.services;

import com.example.datapulse.weather.dto.TrackedCityDTO;
import com.example.datapulse.weather.models.TrackedCityEntity;
import com.example.datapulse.weather.repository.TrackedCityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackedCityServices {

    private final TrackedCityRepository trackedCityRepository;

    public TrackedCityServices(TrackedCityRepository trackedCityRepository) {
        this.trackedCityRepository = trackedCityRepository;
    }


    public List<TrackedCityDTO.TrackedCityOutput> getAllTrackedCityByUser(String userEmail) {

        System.out.println("userEmail reçu = " + userEmail);

        if (userEmail == null || userEmail.isBlank()) {
            throw new IllegalArgumentException("User email cannot be null or blank");
        }

        return trackedCityRepository.findByUserEmail(userEmail)
                .stream()
                .map(this::toResponse)
                .toList();

    }

    private TrackedCityDTO.TrackedCityOutput toResponse(TrackedCityEntity entity) {
        return new TrackedCityDTO.TrackedCityOutput(
                entity.getId(),
                entity.getName(),
                entity.getCountry(),
                entity.getLatitude(),
                entity.getLongitude()
        );
    }

    public TrackedCityEntity createTrackedCity(String name, String country, String userEmail) {

        TrackedCityEntity trackedCityEntity = TrackedCityEntity.builder()
                                                               .name(name)
                                                               .country(country)
                                                               .userEmail(userEmail)
                                                               .build();

        trackedCityRepository.save(trackedCityEntity);

        return trackedCityEntity;

    }


    public void deleteTrackedCity(Integer id) {

        if (!trackedCityRepository.existsById(id)) {
            throw new EntityNotFoundException("Tracked city not found");
        }

        //todo : ajouter la comparaison d'email entre l'email lié a la ville que l'on veut supprimer
        // et l'email de l'utilisateur qui sera connecté avec OAuth2

        trackedCityRepository.deleteById(id);

    }
}
