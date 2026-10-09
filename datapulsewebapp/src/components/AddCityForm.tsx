
import { useState, type FormEvent } from "react";
import "./AddCityForm.css";

interface AddCityFormProps {
  onCancel: () => void;
  onAddCity: (name: string, country: string) => Promise<void>;
}

function AddCityForm({ onCancel, onAddCity }: AddCityFormProps) {
  const [cityName, setCityName] = useState("");
  const [country, setCountry] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);


    async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    setIsSubmitting(true);
    setError(null);

        try {
            await onAddCity(cityName.trim(), country.trim());
        } catch (err) {
            setError(
            err instanceof Error
                ? err.message
                : "Une erreur inattendue est survenue."
            );
        } finally {
            setIsSubmitting(false);
        }
    }


  return (
    <form className="add-city-form" onSubmit={handleSubmit}>
        <h2>Ajouter une ville</h2>

        <div className="add-city-form__field">
            <label htmlFor="cityName">Nom de la ville</label>
            <input
            id="cityName"
            type="text"
            value={cityName}
            onChange={(event) => setCityName(event.target.value)}
            placeholder="Ex. Lille"
            required
            />
        </div>

        <div className="add-city-form__field">
            <label htmlFor="country">Pays</label>
            <input
            id="country"
            type="text"
            value={country}
            onChange={(event) => setCountry(event.target.value)}
            placeholder="Ex. France"
            required
            />
        </div>
        
        {error && (
            <p className="add-city-form__error" role="alert">
                {error}
            </p>
        )}

        <div className="add-city-form__actions">
            <button
                type="button"
                className="add-city-form__cancel-button"
                onClick={onCancel}
                disabled={isSubmitting}
            >
                Annuler
            </button>

            <button
                type="submit"
                className="add-city-form__submit-button"
                disabled={isSubmitting}
            >
                {isSubmitting ? "Ajout en cours..." : "Ajouter"}
            </button>
        </div>
    </form>
  );
}

export default AddCityForm;
