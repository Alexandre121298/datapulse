import type { TrackedCity } from "../types/trackedCity";
import { useState } from "react";
import type { CityWeatherState } from "../types/cityWeather";
import { getWeatherDescription } from "../utils/weatherUtils";
import "./CityCard.css";
import {
  ArrowUpRight,
  Cloud,
  CloudFog,
  CloudLightning,
  CloudRain,
  CloudSnow,
  CloudSun,
  CloudMoon,
  Moon,
  LoaderCircle,
  Sun,
  Trash,
} from "lucide-react";

interface CityCardProps {
  city: TrackedCity;
  weatherState: CityWeatherState;
  isSelected: boolean;
  onSelect: () => void;
  onDelete: (city: TrackedCity) => Promise<void>;
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

function getWeatherIcon(
  code: number | null,
  isDay: boolean | null
) {
  if (code == null) return null;

  if (code === 0 || code === 1) {
    if (isDay === false) {
      return <Moon className="city-card__moon" />;
    }

    if (isDay === true) {
      return <Sun className="city-card__sun" />;
    }

    return null;
  }

  if (code === 2) {
    if (isDay === false) {
      return <CloudMoon className="city-card__moon" />;
    }

    if (isDay === true) {
      return <CloudSun className="city-card__sun" />;
    }

    return <Cloud />;
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

function CityCard({
  city,
  weatherState,
  isSelected,
  onSelect,
  onDelete,
}: CityCardProps) {
  const [isDeleting, setIsDeleting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const {
    weather,
    isLoading: isWeatherLoading,
    error: weatherError,
  } = weatherState;


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
      className={`city-card${isSelected ? " city-card--selected" : ""}`}
      aria-labelledby={`city-title-${city.id}`}
    >
      <div className="city-card__body">
        <button
          className="city-card__select"
          type="button"
          onClick={onSelect}
          aria-pressed={isSelected}
          aria-labelledby={`city-title-${city.id} city-country-${city.id}`}
          aria-describedby={`city-temperature-${city.id} city-weather-${city.id}`}
        >
          <span className="city-card__overview">
            <span
              className="city-card__weather-symbol"
              aria-hidden="true"
            >
              {getWeatherIcon(
                weather?.weatherCode ?? null,
                weather?.isDay ?? null
              )}
            </span>

            <span
              className="city-card__temperature"
              id={`city-temperature-${city.id}`}
            >
              {formatWeatherValue(weather?.temperature ?? null)}

              {weather?.temperature != null && (
                <span className="city-card__temperature-unit">
                  °C
                </span>
              )}
            </span>
          </span>

          <span className="city-card__identity">
            <span
              className="city-card__title"
              id={`city-title-${city.id}`}
            >
              {city.name}
            </span>

            <span
              className="city-card__country"
              id={`city-country-${city.id}`}
            >
              {city.country}
            </span>
          </span>

          <span
            className="city-card__condition"
            id={`city-weather-${city.id}`}
          >
            {isWeatherLoading
              ? "Chargement de la météo…"
              : weatherError || !weather
                ? "Météo indisponible"
                : getWeatherDescription(weather.weatherCode)}
          </span>

          <span className="city-card__select-hint">
            {isSelected ? "Ville affichée" : "Afficher la météo"}

            {!isSelected && (
              <ArrowUpRight size={16} aria-hidden="true" />
            )}
          </span>
        </button>

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
              size={17}
              className="city-card__spinner"
              aria-hidden="true"
            />
          ) : (
            <Trash size={17} aria-hidden="true" />
          )}
        </button>
      </div>

      {error && (
        <p className="city-card__error" role="alert">
          {error}
        </p>
      )}
    </article>
  );
}

export default CityCard;
