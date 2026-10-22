
import { useState, useEffect } from "react";
import type { TrackedCity } from "../types/trackedCity";
import type { Weather } from "../types/weather";
import { getCurrentWeather } from "../services/weatherService";
import { getWeatherDescription } from "../utils/weatherUtils";
import "./CityCard.css";
import {
  Cloud,
  CloudFog,
  CloudLightning,
  CloudRain,
  CloudSnow,
  CloudSun,
  Droplets,
  LoaderCircle,
  Sun,
  Trash,
  Wind,
} from "lucide-react";

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

const weatherNumberFormatter = new Intl.NumberFormat("fr-FR", {
  maximumFractionDigits: 1,
});

function formatWeatherValue(
  value: number | null,
  unit = ""
): string {
  if (value == null) {
    return "—";
  }

  const formattedValue = weatherNumberFormatter.format(value);

  return unit ? `${formattedValue} ${unit}` : formattedValue;
}

function getWeatherIcon(code: number | null) {
  if (code == null) return null;

  if (code === 0 || code === 1) {
    return <Sun className="city-card__sun" />;
  }

  if (code === 2) {
    return <CloudSun className="city-card__sun" />;
  }

  if (code === 3) return <Cloud />;

  if ([45, 48].includes(code)) return <CloudFog />;

  if (
    [51, 53, 55, 56, 57, 61, 63, 65, 66, 67, 80, 81, 82].includes(code)
  ) {
    return <CloudRain />;
  }

  if ([71, 73, 75, 77, 85, 86].includes(code)) {
    return <CloudSnow />;
  }

  if ([95, 96, 97, 99].includes(code)) {
    return <CloudLightning />;
  }

  return null;
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
    <article
      className="city-card"
      aria-labelledby={`city-title-${city.id}`}
    >
      <header className="city-card__header">
        <div>
          <h3
            className="city-card__title"
            id={`city-title-${city.id}`}
          >
            {city.name}
          </h3>

          <p className="city-card__country">{city.country}</p>
        </div>

        <button
          className="city-card__delete-button"
          type="button"
          onClick={handleDelete}
          disabled={isDeleting}
          aria-label={
            isDeleting
              ? `Suppression de ${city.name} en cours`
              : `Ne plus suivre ${city.name}`
          }
          title={`Ne plus suivre ${city.name}`}
        >
          {isDeleting ? (
            <LoaderCircle
              size={18}
              className="city-card__spinner"
              aria-hidden="true"
            />
          ) : (
            <Trash size={18} aria-hidden="true" />
          )}
        </button>
      </header>

      <section
        className="city-card__weather"
        aria-label={`Météo actuelle à ${city.name}`}
        aria-busy={isWeatherLoading}
      >
        {city.latitude == null || city.longitude == null ? (
          <p className="city-card__state">
            La météo de cette ville n’est pas disponible.
          </p>
        ) : isWeatherLoading ? (
          <p className="city-card__state" role="status">
            Chargement de la météo...
          </p>
        ) : weatherError ? (
          <p
            className="city-card__state city-card__error"
            role="alert"
          >
            {weatherError}
          </p>
        ) : weather ? (
          <>
            <div className="city-card__weather-hero">
              <div>
                <p
                  className="city-card__temperature"
                  aria-label={
                    weather.temperature == null
                      ? "Température indisponible"
                      : `Température : ${formatWeatherValue(
                          weather.temperature
                        )} degrés Celsius`
                  }
                >
                  {formatWeatherValue(weather.temperature)}

                  {weather.temperature != null && (
                    <span className="city-card__temperature-unit">
                      °C
                    </span>
                  )}
                </p>

                <p className="city-card__condition">
                  {getWeatherDescription(weather.weatherCode)}
                </p>
              </div>

              <span
                className="city-card__weather-symbol"
                aria-hidden="true"
              >
                {getWeatherIcon(weather.weatherCode)}
              </span>
            </div>

            <p className="city-card__feels-like">
              Ressenti :{" "}
              {formatWeatherValue(weather.apparentTemperature, "°C")}
            </p>

            <dl className="city-card__stats">
              <div>
                <dt>
                  <Droplets size={16} aria-hidden="true" />
                  Humidité
                </dt>

                <dd>
                  {formatWeatherValue(weather.humidity, "%")}
                </dd>
              </div>

              <div>
                <dt>
                  <Wind size={16} aria-hidden="true" />
                  Vent
                </dt>

                <dd>
                  {formatWeatherValue(weather.windSpeed, "km/h")}
                </dd>
              </div>
            </dl>
          </>
        ) : null}
      </section>

      {error && (
        <p className="city-card__error" role="alert">
          {error}
        </p>
      )}
    </article>
  );
}

export default CityCard;
