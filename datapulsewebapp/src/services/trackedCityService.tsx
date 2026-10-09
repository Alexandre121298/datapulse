
import type { TrackedCity } from "../types/trackedCity";

export async function createTrackedCity(
  name: string,
  country: string,
  userEmail: string
): Promise<TrackedCity> {

  const response = await fetch(
    "/api/tracked-cities/create-tracked-city",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        name,
        country,
        userEmail,
      }),
    }
  );

  if (response.status === 409) {
    throw new Error("Cette ville est déjà suivie.");
  }

  if (!response.ok) {
    throw new Error(
      `Erreur lors de l'ajout de la ville (${response.status}).`
    );
  }

  return response.json() as Promise<TrackedCity>;
}


export async function deleteTrackedCity(
  cityName: string,
  cityCountry: string,
  userEmail: string
): Promise<void> {

  const params = new URLSearchParams({
    userEmail,
    cityName,
    cityCountry,
  });

  const response = await fetch(
    `/api/tracked-cities/delete?${params}`,
    {
      method: "DELETE",
    }
  );

  if (response.status === 404) {
    throw new Error("Cette ville n'existe plus.");
  }

  if (!response.ok) {
    throw new Error(
      `Erreur lors de la suppression (${response.status}).`
    );
  }
}
