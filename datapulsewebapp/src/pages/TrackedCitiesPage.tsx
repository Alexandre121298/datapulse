import CityCard from "../components/CityCard";
import type { TrackedCity } from "../types/trackedCity";
import "./TrackedCitiesPage.css";

const trackedCities: TrackedCity[] = [
  {
    id: 1,
    name: "Lille",
    country: "France",
    latitude: 50.63391,
    longitude: 3.05512,
  },
  {
    id: 2,
    name: "Amiens",
    country: "France",
    latitude: 49.89407,
    longitude: 2.29575,
  },
  {
    id: 3,
    name: "Bruxelles",
    country: "Belgique",
    latitude: 50.85045,
    longitude: 4.34878,
  },
];

function TrackedCitiesPage() {
  return (
    <main className="tracked-cities-page">
      <header className="tracked-cities-page__header">
        <div>
          <h1>DataPulse</h1>
          <p>Suivez la météo de vos villes préférées.</p>
        </div>

        <button type="button">Ajouter une ville</button>
      </header>

      <section>
        <h2>Mes villes suivies</h2>

        {trackedCities.length === 0 ? (
          <p>Aucune ville suivie pour le moment.</p>
        ) : (
          <div className="tracked-cities-page__grid">
            {trackedCities.map((city) => (
              <CityCard key={city.id} city={city} />
            ))}
          </div>
        )}
      </section>
    </main>
  );
}

export default TrackedCitiesPage;