import { useEffect, useRef, useState } from "react";
import type { TrackedCity } from "../types/trackedCity";
import type { Weather } from "../types/weather";
import type { CityWeatherState } from "../types/cityWeather";
import { getCurrentWeather } from "../services/weatherService";

interface WeatherResult {
  data: Weather | null;
  error: string | null;
}

function getCoordinates(city: TrackedCity) {
  if (city.latitude == null || city.longitude == null) {
    return null;
  }

  return {
    key: `${city.latitude}:${city.longitude}`,
    latitude: city.latitude,
    longitude: city.longitude,
  };
}

export function useCitiesWeather(cities: TrackedCity[]) {
  const [results, setResults] = useState<
    Record<string, WeatherResult>
  >({});

  const completedRef = useRef(new Set<string>());

  const requestsRef = useRef(
    new Map<string, AbortController>()
  );

  // Annuler les requêtes lorsque la page est démontée.
  useEffect(() => {
    const requests = requestsRef.current;

    return () => {
      requests.forEach((controller) => controller.abort());
      requests.clear();
    };
  }, []);

  useEffect(() => {
    const requests = requestsRef.current;
    const completed = completedRef.current;

    const coordinatesByKey = new Map<
      string,
      { latitude: number; longitude: number }
    >();

    for (const city of cities) {
      const coordinates = getCoordinates(city);

      if (coordinates !== null) {
        coordinatesByKey.set(coordinates.key, coordinates);
      }
    }

    // Une ville supprimée ne doit pas conserver de requête en cours.
    for (const [key, controller] of requests) {
      if (!coordinatesByKey.has(key)) {
        controller.abort();
        requests.delete(key);
      }
    }

    async function loadWeather(
      key: string,
      latitude: number,
      longitude: number,
      controller: AbortController
    ) {
      let result: WeatherResult;

      try {
        const data = await getCurrentWeather(
          latitude,
          longitude,
          controller.signal
        );

        result = { data, error: null };
      } catch (err) {
        result = {
          data: null,
          error:
            err instanceof Error
              ? err.message
              : "Impossible de récupérer la météo.",
        };
      }

      if (!controller.signal.aborted) {
        completed.add(key);

        setResults((previousResults) => ({
          ...previousResults,
          [key]: result,
        }));
      }

      // Une ancienne requête ne doit pas effacer sa remplaçante.
      if (requests.get(key) === controller) {
        requests.delete(key);
      }
    }

    for (const [key, coordinates] of coordinatesByKey) {
      if (completed.has(key) || requests.has(key)) {
        continue;
      }

      const controller = new AbortController();

      requests.set(key, controller);

      void loadWeather(
        key,
        coordinates.latitude,
        coordinates.longitude,
        controller
      );
    }
  }, [cities]);

  function getWeatherState(city: TrackedCity): CityWeatherState {
    const coordinates = getCoordinates(city);

    if (coordinates === null) {
      return {
        weather: null,
        isLoading: false,
        error: null,
      };
    }

    const result = results[coordinates.key];

    return {
      weather: result?.data ?? null,
      isLoading: result === undefined,
      error: result?.error ?? null,
    };
  }

  return getWeatherState;
}