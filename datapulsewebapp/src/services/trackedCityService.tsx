import type { TrackedCity } from "../types/trackedCity";
import { apiFetch, readErrorMessage } from "./apiClient";

export async function createTrackedCity(
  name: string,
  country: string
): Promise<TrackedCity> {
  const response = await apiFetch(
    "/api/tracked-cities/create-tracked-city",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ name, country }),
    }
  );

  if (response.status === 404) {
    throw new Error(
      "Aucune ville trouvée pour ce nom et ce pays."
    );
  }

  if (response.status === 409) {
    throw new Error("Cette ville est déjà suivie.");
  }

  if (!response.ok) {
    throw new Error(
      await readErrorMessage(
        response,
        "Impossible d’ajouter la ville."
      )
    );
  }

  return (await response.json()) as TrackedCity;
}

export async function deleteTrackedCity(
  cityName: string,
  cityCountry: string
): Promise<void> {
  const params = new URLSearchParams({
    cityName,
    cityCountry,
  });

  const response = await apiFetch(
    `/api/tracked-cities/delete?${params}`,
    { method: "DELETE" }
  );

  if (response.status === 404) {
    throw new Error("Cette ville n’existe plus.");
  }

  if (!response.ok) {
    throw new Error(
      await readErrorMessage(
        response,
        "Impossible de supprimer la ville."
      )
    );
  }
}

interface TrackedCitiesResponse {
  content: TrackedCity[];
}

export async function getTrackedCities(
  signal?: AbortSignal
): Promise<TrackedCity[]> {
  const response = await apiFetch(
    "/api/tracked-cities/all-tracked?size=100",
    { signal }
  );

  if (!response.ok) {
    throw new Error("Impossible de récupérer les villes.");
  }

  const data = (await response.json()) as TrackedCitiesResponse;

  return data.content;
}