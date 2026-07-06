package com.example.datapulse.weather.services;

import com.example.datapulse.common.exception.ConflictException;
import com.example.datapulse.common.exception.TrackedCityNotFoundException;
import com.example.datapulse.weather.dto.TrackedCityDTO;
import com.example.datapulse.weather.models.TrackedCityEntity;
import com.example.datapulse.weather.repository.TrackedCityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class TrackedCityServices {

    private final TrackedCityRepository trackedCityRepository;

    public TrackedCityServices(TrackedCityRepository trackedCityRepository) {
        this.trackedCityRepository = trackedCityRepository;
    }


    public List<TrackedCityDTO.TrackedCityOutput> getAllTrackedCitiesByUser(String userEmail, Pageable pageable) throws BadRequestException {

        System.out.println("userEmail reçu = " + userEmail);

        if (userEmail == null || userEmail.isBlank()) {
            throw new BadRequestException("L'email de l'utilisateur ne peux pas être vide");
        }

        return trackedCityRepository.findByUserEmail(userEmail, pageable)
                .stream()
                .map(this::toResponse)
                .toList();

    }

    private TrackedCityDTO.TrackedCityOutput toResponse(TrackedCityEntity entity) {
        return new TrackedCityDTO.TrackedCityOutput(
                entity.getId(),
                entity.getCityName(),
                entity.getCityCountry(),
                entity.getLatitude(),
                entity.getLongitude()
        );
    }

    //todo : Modifier la méthode quand l'authentification sera mise en place
    public TrackedCityDTO.TrackedCityOutput getTrackedCityByUser(String userEmail, String cityName,String cityCountry) throws BadRequestException {

        //todo : A supprimer quand l'authentification sera mis en place
        if(userEmail == null || userEmail.isBlank())
        {
            throw new BadRequestException("L'email de l'utilisateur ne peux pas être vide");
        }

        if(cityName == null || cityName.isBlank())
        {
            throw new BadRequestException("Le nom de la ville ne peux pas être null ou vide");
        }

        if(cityCountry == null || cityCountry.isBlank())
        {
            throw new BadRequestException("Le pays de la ville demandée ne peux pas être null ou vide");
        }



        TrackedCityEntity trackedCity = trackedCityRepository.findByUserEmailAndCityNameAndCityCountry(userEmail,cityName,cityCountry);

        if(trackedCity == null)
        {
            throw new TrackedCityNotFoundException(userEmail,cityName,cityCountry);
        }

        return TrackedCityDTO.TrackedCityOutput.builder()
                .id(trackedCity.getId())
                .name(trackedCity.getCityName())
                .country(trackedCity.getCityCountry())
                .latitude(trackedCity.getLatitude())
                .longitude(trackedCity.getLongitude())
                .build();
    }

    public TrackedCityEntity createTrackedCity(String cityName, String cityCountry, String userEmail) throws BadRequestException {

        //todo : A supprimer quand l'authentification sera mis en place
        if(userEmail == null || userEmail.isBlank())
        {
            throw new BadRequestException("L'email de l'utilisateur ne peux pas être vide");
        }

        if(cityName == null || cityName.isBlank())
        {
            throw new BadRequestException("Le nom de la ville ne peux pas être null ou vide");
        }

        if(cityCountry == null || cityCountry.isBlank())
        {
            throw new BadRequestException("Le pays de la ville demandée ne peux pas être null ou vide");
        }


        //todo : Try/Catch pour gerer si la creation de la ville s'est bien passée ?
        TrackedCityEntity trackedCityEntity = TrackedCityEntity.builder()
                                                               .cityName(cityName)
                                                               .cityCountry(cityCountry)
                                                               .userEmail(userEmail)
                                                               .build();

        if (trackedCityRepository.existsByUserEmailAndCityNameAndCityCountry(userEmail,cityName,cityCountry)) {
            throw new ConflictException("Cette ville est déjà suivie par cet utilisateur.");
        }

        trackedCityRepository.save(trackedCityEntity);

        return trackedCityEntity;

    }


    public void deleteTrackedCity(String userEmail, String cityName, String cityCountry) throws BadRequestException {

        //todo : A supprimer quand l'authentification sera mis en place
        if(userEmail == null || userEmail.isBlank())
        {
            throw new BadRequestException("L'email de l'utilisateur ne peux pas être vide");
        }

        if(cityName == null || cityName.isBlank())
        {
            throw new BadRequestException("Le nom de la ville ne peux pas être null ou vide");
        }

        if(cityCountry == null || cityCountry.isBlank())
        {
            throw new BadRequestException("Le pays de la ville demandée ne peux pas être null ou vide");
        }

        TrackedCityEntity trackedCity = trackedCityRepository.findByUserEmailAndCityNameAndCityCountry(userEmail,cityName,cityCountry);

        if (trackedCity == null) {
            throw new TrackedCityNotFoundException(userEmail,cityName,cityCountry);
        }

        //todo : ajouter la comparaison d'email entre l'email lié a la ville que l'on veut supprimer
        // et l'email de l'utilisateur qui sera connecté avec OAuth2

        trackedCityRepository.delete(trackedCity);

    }
}
