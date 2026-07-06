package com.example.datapulse.weather.repository;

import com.example.datapulse.weather.models.TrackedCityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface TrackedCityRepository extends JpaRepository<TrackedCityEntity, Integer> {

    List<TrackedCityEntity> findByUserEmail(String userEmail, Pageable pageable);

    TrackedCityEntity findByUserEmailAndCityNameAndCityCountry(String userEmail, String cityName, String cityCountry);

    boolean existsByUserEmailAndCityNameAndCityCountry(String userEmail, String cityName, String cityCountry);
}
