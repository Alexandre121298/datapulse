import type { Weather } from "../types/weather";

export async function getCurrentWeather(
  latitude: number,
  longitude: number,
  signal?: AbortSignal
): Promise<Weather> {
  const params = new URLSearchParams({
    latitude: String(latitude),
    longitude: String(longitude),
  });

  const response = await fetch(
    `/api/weather/current?${params}`,
    { signal }
  );

  if (response.status === 502) {
    throw new Error(
      "Le service météo est temporairement indisponible."
    );
  }

  if (!response.ok) {
    throw new Error("Impossible de récupérer la météo.");
  }

  return response.json() as Promise<Weather>;
}