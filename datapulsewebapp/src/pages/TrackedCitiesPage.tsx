
import { useEffect, useState } from "react";
import CityCard from "../components/CityCard";
import type { TrackedCity } from "../types/trackedCity";
import AddCityForm from "../components/AddCityForm";
import {
  createTrackedCity,
  deleteTrackedCity,
  getTrackedCities
} from "../services/trackedCityService";
import "./TrackedCitiesPage.css";

// TODO: Temporaire, en attendant l'authentification
const USER_EMAIL = "test@test.fr";

function TrackedCitiesPage() {
  const [trackedCities, setTrackedCities] = useState<TrackedCity[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isAddFormOpen, setIsAddFormOpen] = useState(false);

  useEffect(() => {
    const controller = new AbortController();

    async function loadCities() {
      try {
        const cities = await getTrackedCities(
          USER_EMAIL,
          controller.signal
        );

        setTrackedCities(cities);
      } catch (err) {
        if (!controller.signal.aborted) {
          console.error(err);
          setError("Impossible de récupérer les villes.");
        }
      } finally {
        if (!controller.signal.aborted) {
          setIsLoading(false);
        }
      }
    }

    void loadCities();

    return () => controller.abort();
  }, []);

      async function handleAddCity(name: string, country: string) {
      const newCity = await createTrackedCity(
        name,
        country,
        USER_EMAIL
      );

      setTrackedCities((previousCities) => [
        ...previousCities,
        newCity
      ]);

      setIsAddFormOpen(false);
    }

    
    async function handleDeleteCity(city: TrackedCity): Promise<void> {
      await deleteTrackedCity(
        city.name,
        city.country,
        USER_EMAIL
      );

      setTrackedCities((previousCities) =>
        previousCities.filter(
          (trackedCity) => trackedCity.id !== city.id
        )
      );
    }


  return (
    <main className="tracked-cities-page">
      <header className="tracked-cities-page__header">
        <div>
          <h1>DataPulse</h1>
          <p>Suivez la météo de vos villes préférées.</p>
        </div>

        <button type="button" onClick={() => setIsAddFormOpen(true)}>
          Ajouter une ville
        </button>
      </header>

      {isAddFormOpen && (
        <AddCityForm
          onCancel={() => setIsAddFormOpen(false)}
          onAddCity={handleAddCity}
        />
      )}

      <section>
        <h2>Mes villes suivies</h2>

        {isLoading ? (
          <p>Chargement des villes...</p>
        ) : error ? (
          <p role="alert">{error}</p>
        ) : trackedCities.length === 0 ? (
          <p>Aucune ville suivie pour le moment.</p>
        ) : (
          <div className="tracked-cities-page__grid">
            {trackedCities.map((city) => (
              <CityCard
                key={city.id}
                city={city}
                onDelete={handleDeleteCity}
              />
            ))}
          </div>
        )}
      </section>
    </main>
  );
}

export default TrackedCitiesPage;
