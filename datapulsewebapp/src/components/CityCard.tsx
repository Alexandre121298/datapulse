
import { useState } from "react";
import type { TrackedCity } from "../types/trackedCity";
import "./CityCard.css";

interface CityCardProps {
  city: TrackedCity;
  onDelete: (city: TrackedCity) => Promise<void>;
}

function CityCard({ city, onDelete }: CityCardProps) {
  const [isDeleting, setIsDeleting] = useState(false);
  const [error, setError] = useState<string | null>(null);

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
    <article className="city-card">
      <div className="city-card__header">
        <h2>{city.name}</h2>
        <span>{city.country}</span>
      </div>

      {error && <p role="alert">{error}</p>}

      <button
        className="city-card__delete-button"
        type="button"
        onClick={handleDelete}
        disabled={isDeleting}
      >
        {isDeleting ? "Suppression..." : "Supprimer"}
      </button>
    </article>
  );
}

export default CityCard;
