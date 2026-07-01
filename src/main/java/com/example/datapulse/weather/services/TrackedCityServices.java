package com.example.datapulse.weather.services;

import com.example.datapulse.weather.models.TrackedCityEntity;
import com.example.datapulse.weather.repository.TrackedCityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackedCityServices {

    private final TrackedCityRepository trackedCityRepository;

    public TrackedCityServices(TrackedCityRepository trackedCityRepository) {
        this.trackedCityRepository = trackedCityRepository;
    }


    public List<TrackedCityEntity> getAllTrackedCityByUser(String userEmail) {

        //Todo : Ecrire early checks

        return trackedCityRepository.findByUserEmail(userEmail);

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


}
