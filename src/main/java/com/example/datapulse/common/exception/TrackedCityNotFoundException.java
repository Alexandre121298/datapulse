package com.example.datapulse.common.exception;

public class TrackedCityNotFoundException extends RuntimeException {

    public TrackedCityNotFoundException(String userEmail,String cityName, String cityCountry) {
        super("Aucune ville "+ cityName+ " suivie par :" +userEmail + " n'a été trouvée dans le pays : " + cityCountry);
    }

    //Todo : Implementer l'exception pour la creation d'une ville suivie par un utilisateur
}
