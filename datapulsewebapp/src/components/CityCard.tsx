import type { TrackedCity } from "../types/trackedCity";
import "./CityCard.css";

interface CityCardProps {
  city: TrackedCity;
}

function CityCard({ city }: CityCardProps) {
  return (
    <article className="city-card">
      <div className="city-card__header">
        <h2>{city.name}</h2>
        <span>{city.country}</span>
      </div>

      <div className="city-card__coordinates">
        <p>
          <strong>Latitude :</strong> {city.latitude}
        </p>

        <p>
          <strong>Longitude :</strong> {city.longitude}
        </p>
      </div>

      <button className="city-card__delete-button" type="button">
        Supprimer
      </button>
    </article>
  );
}

export default CityCard;