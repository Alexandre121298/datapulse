import { useState } from "react";
import { Droplets, MapPin, Pause, Play, Wind } from "lucide-react";
import type { TrackedCity } from "../types/trackedCity";
import type { CityWeatherState } from "../types/cityWeather";
import { getWeatherDescription } from "../utils/weatherUtils";
import "./WeatherHero.css";

interface WeatherHeroProps {
  city: TrackedCity;
  weatherState: CityWeatherState;
}

const numberFormatter = new Intl.NumberFormat("fr-FR", {
  maximumFractionDigits: 1,
});

function formatValue(value: number | null, unit = "") {
  if (value == null) return "—";

  const formatted = numberFormatter.format(value);

  return unit ? `${formatted} ${unit}` : formatted;
}

function getWeatherScene(code: number | null) {
  if (code == null) return "neutral";

  if ([0, 1].includes(code)) return "clear";
  if ([2, 3].includes(code)) return "cloudy";
  if ([45, 48].includes(code)) return "fog";

  if (
    [51, 53, 55, 56, 57, 61, 63, 65, 66, 67, 80, 81, 82].includes(code)
  ) {
    return "rain";
  }

  if ([71, 73, 75, 77, 85, 86].includes(code)) {
    return "snow";
  }

  if ([95, 96, 97, 99].includes(code)) {
    return "storm";
  }

  return "neutral";
}

function WeatherHero({ city, weatherState }: WeatherHeroProps) {
  const [isPaused, setIsPaused] = useState(false);
  const { weather, isLoading, error } = weatherState;

  const scene = getWeatherScene(
    !isLoading && !error ? weather?.weatherCode ?? null : null
  );

  const period =
  isLoading || error || !weather
    ? "unknown"
    : weather.isDay === true
      ? "day"
      : weather.isDay === false
        ? "night"
        : "unknown";

  return (
    <section
      className={[
        "weather-hero",
        `weather-hero--${scene}`,
        `weather-hero--${period}`,
        isPaused ? "weather-hero--paused" : "",
      ].join(" ")}
      aria-labelledby="weather-hero-title"
      aria-busy={isLoading}
    >
      <div className="weather-hero__sky" aria-hidden="true">
        {weather && [0, 1, 2].includes(weather.weatherCode ?? -1) && (
          <span className="weather-hero__orb" />
        )}

        <span className="weather-hero__stars" />

        <span className="weather-hero__cloud weather-hero__cloud--first" />
        <span className="weather-hero__cloud weather-hero__cloud--second" />

        <span className="weather-hero__precipitation" />
        <span className="weather-hero__horizon" />
      </div>

      <button
        className="weather-hero__motion-button"
        type="button"
        onClick={() => setIsPaused((previous) => !previous)}
        aria-label={
          isPaused
            ? "Reprendre les animations météo"
            : "Mettre les animations météo en pause"
        }
      >
        {isPaused ? (
          <Play size={16} aria-hidden="true" />
        ) : (
          <Pause size={16} aria-hidden="true" />
        )}

        {isPaused ? "Reprendre" : "Pause"}
      </button>

      <div className="weather-hero__content">
        <p className="weather-hero__location">
          <MapPin size={15} aria-hidden="true" />
          {city.country}
        </p>

        <h2
          className="weather-hero__title"
          id="weather-hero-title"
        >
          {city.name}
        </h2>

        {isLoading ? (
          <p className="weather-hero__message" role="status">
            Chargement de la météo…
          </p>
        ) : error ? (
          <p className="weather-hero__message" role="alert">
            {error}
          </p>
        ) : weather ? (
          <>
            <p
              className="weather-hero__temperature"
              aria-label={
                weather.temperature == null
                  ? "Température indisponible"
                  : `${formatValue(weather.temperature)} degrés Celsius`
              }
            >
              {formatValue(weather.temperature)}

              {weather.temperature != null && (
                <span>°C</span>
              )}
            </p>

            <p className="weather-hero__condition">
              {getWeatherDescription(weather.weatherCode)}
            </p>

            <p className="weather-hero__feels-like">
              Ressenti :{" "}
              {formatValue(weather.apparentTemperature, "°C")}
            </p>
          </>
        ) : (
          <p className="weather-hero__message">
            La météo de cette ville n’est pas disponible.
          </p>
        )}
      </div>

      {!isLoading && !error && weather && (
        <dl className="weather-hero__stats">
          <div>
            <dt>
              <Droplets size={18} aria-hidden="true" />
              Humidité
            </dt>
            <dd>{formatValue(weather.humidity, "%")}</dd>
          </div>

          <div>
            <dt>
              <Wind size={18} aria-hidden="true" />
              Vent
            </dt>
            <dd>{formatValue(weather.windSpeed, "km/h")}</dd>
          </div>
        </dl>
      )}
    </section>
  );
}

export default WeatherHero;