
import { useEffect, useState } from "react";
import CityCard from "../components/CityCard";
import type { TrackedCity } from "../types/trackedCity";
import AddCityForm from "../components/AddCityForm";
import {
  createTrackedCity,
  deleteTrackedCity,
  getTrackedCities
} from "../services/trackedCityService";
import { useCitiesWeather } from "../hooks/useCitiesWeather";
import WeatherHero from "../components/WeatherHero";
import "./TrackedCitiesPage.css";



function TrackedCitiesPage() {
  const [trackedCities, setTrackedCities] = useState<TrackedCity[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isAddFormOpen, setIsAddFormOpen] = useState(false);

  const [selectedCityId, setSelectedCityId] =
    useState<number | null>(null);

  const getWeatherState = useCitiesWeather(trackedCities);

  const selectedCity =
    trackedCities.find((city) => city.id === selectedCityId) ??
    trackedCities[0] ??
    null;

  useEffect(() => {
    const controller = new AbortController();

    async function loadCities() {
      try {
        const cities = await getTrackedCities(controller.signal);

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
      const newCity = await createTrackedCity(name, country);

      setTrackedCities((previousCities) => [
        ...previousCities,
        newCity
      ]);

      setSelectedCityId(newCity.id);

      setIsAddFormOpen(false);
    }

    
    async function handleDeleteCity(city: TrackedCity): Promise<void> {
      await deleteTrackedCity(city.name, city.country);

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
          <p className="tracked-cities-page__eyebrow">
            Votre espace météo
          </p>

          <h1>La météo de vos villes.</h1>

          <p className="tracked-cities-page__intro">
            Retrouvez les conditions actuelles des lieux qui comptent pour vous.
          </p>
        </div>

        <button
          className="tracked-cities-page__add-button"
          type="button"
          onClick={() => setIsAddFormOpen(true)}
          disabled={isAddFormOpen}
        >
          <span aria-hidden="true">+</span>
          Ajouter une ville
        </button>
      </header>

      {isAddFormOpen && (
        <AddCityForm
          onCancel={() => setIsAddFormOpen(false)}
          onAddCity={handleAddCity}
        />
      )}

      {!isLoading && !error && selectedCity !== null && (
        <WeatherHero
          city={selectedCity}
          weatherState={getWeatherState(selectedCity)}
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
                weatherState={getWeatherState(city)}
                isSelected={selectedCity?.id === city.id}
                onSelect={() => setSelectedCityId(city.id)}
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
