
import { useState, useEffect } from "react";
import type { TrackedCity } from "../types/trackedCity";
import type { Weather } from "../types/weather";
import { getCurrentWeather } from "../services/weatherService";
import "./CityCard.css";

interface CityCardProps {
  city: TrackedCity;
  onDelete: (city: TrackedCity) => Promise<void>;
}

interface WeatherResult {
  latitude: number;
  longitude: number;
  data: Weather | null;
  error: string | null;
}

function CityCard({ city, onDelete }: CityCardProps) {
  const [isDeleting, setIsDeleting] = useState(false);
  const [error, setError] = useState<string | null>(null);
  
  const [weatherResult, setWeatherResult] =
  useState<WeatherResult | null>(null);

const matchingResult =
  weatherResult !== null &&
  weatherResult.latitude === city.latitude &&
  weatherResult.longitude === city.longitude
    ? weatherResult
    : null;

const weather = matchingResult?.data ?? null;
const weatherError = matchingResult?.error ?? null;

const isWeatherLoading =
  city.latitude != null &&
  city.longitude != null &&
  matchingResult === null;


useEffect(() => {
  const latitude = city.latitude;
  const longitude = city.longitude;

  if (latitude == null || longitude == null) {
    return;
  }

  const controller = new AbortController();

  async function loadWeather(
    latitude: number,
    longitude: number
  ) {
    try {
      const data = await getCurrentWeather(
        latitude,
        longitude,
        controller.signal
      );

      if (!controller.signal.aborted) {
        setWeatherResult({
          latitude,
          longitude,
          data,
          error: null,
        });
      }
    } catch (err) {
      if (!controller.signal.aborted) {
        setWeatherResult({
          latitude,
          longitude,
          data: null,
          error:
            err instanceof Error
              ? err.message
              : "Impossible de récupérer la météo.",
        });
      }
    }
  }

  void loadWeather(latitude, longitude);

  return () => controller.abort();
}, [city.latitude, city.longitude]);


  async function handleDelete() {
    const confirmed = window.confirm(
      `Voulez-vous supprimer ${city.name} (${city.country}) ?`
    );

    if (!confirmed) return;

    setIsDeleting(true);
    setError(null);

    try {
      await onDelete(city);
    } catch (err) {
      setError(
        err instanceof Error
          ? err.message
          : "Une erreur est survenue."
      );
    } finally {
      setIsDeleting(false);
    }
  }

  return (
    <article className="city-card">
      <div className="city-card__header">
        <h2>{city.name}</h2>
        <span>{city.country}</span>
      </div>

      <div className="city-card__coordinates">
        <p>Latitude : {city.latitude ?? "Non renseignée"}</p>
        <p>Longitude : {city.longitude ?? "Non renseignée"}</p>
      </div>

      <section
        className="city-card__weather"
        aria-label="Météo actuelle"
      >
        {city.latitude == null || city.longitude == null ? (
          <p>Météo indisponible : coordonnées manquantes.</p>
        ) : isWeatherLoading ? (
          <p role="status">Chargement de la météo...</p>
        ) : weatherError ? (
          <p role="alert">{weatherError}</p>
        ) : weather ? (
          <>
            <p>
              Température :{" "}
              {weather.temperature == null
                ? "Non disponible"
                : `${weather.temperature} °C`}
            </p>

            <p>
              Ressenti :{" "}
              {weather.apparentTemperature == null
                ? "Non disponible"
                : `${weather.apparentTemperature} °C`}
            </p>

            <p>
              Humidité :{" "}
              {weather.humidity == null
                ? "Non disponible"
                : `${weather.humidity} %`}
            </p>

            <p>
              Vent :{" "}
              {weather.windSpeed == null
                ? "Non disponible"
                : `${weather.windSpeed} km/h`}
            </p>
          </>
        ) : null}
      </section>

      {error && <p role="alert">{error}</p>}

      <button
        className="city-card__delete-button"
        type="button"
        onClick={handleDelete}
        disabled={isDeleting}
      >
        {isDeleting ? "Suppression..." : "Supprimer"}
      </button>
    </article>
  );
}

export default CityCard;
