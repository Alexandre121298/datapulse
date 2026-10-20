import { useState, type FormEvent } from "react";
import "./LoginForm.css";

interface RegisterFormProps {
  onRegister: (email: string, password: string) => Promise<void>;
}

function RegisterForm({ onRegister }: RegisterFormProps) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [passwordConfirmation, setPasswordConfirmation] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setError(null);

    if (password !== passwordConfirmation) {
      setError("Les mots de passe ne correspondent pas.");
      return;
    }

    setIsSubmitting(true);

    try {
      await onRegister(email.trim(), password);
    } catch (err) {
      setError(
        err instanceof Error
          ? err.message
          : "Impossible de créer le compte."
      );
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <form
      className="login-form"
      onSubmit={handleSubmit}
      aria-busy={isSubmitting}
    >
      <h1>Créer un compte DataPulse</h1>
      <p>Retrouvez vos villes dans votre espace personnel.</p>

      <div className="login-form__field">
        <label htmlFor="register-email">Email</label>
        <input
          id="register-email"
          type="email"
          autoComplete="username"
          maxLength={254}
          value={email}
          onChange={(event) => setEmail(event.target.value)}
          disabled={isSubmitting}
          required
        />
      </div>

      <div className="login-form__field">
        <label htmlFor="register-password">Mot de passe</label>
        <input
          id="register-password"
          type="password"
          autoComplete="new-password"
          minLength={15}
          maxLength={128}
          aria-describedby="register-password-help"
          value={password}
          onChange={(event) => setPassword(event.target.value)}
          disabled={isSubmitting}
          required
        />
        <small id="register-password-help">
          Entre 15 et 128 caractères. Vous pouvez utiliser une phrase.
        </small>
      </div>

      <div className="login-form__field">
        <label htmlFor="register-password-confirmation">
          Confirmer le mot de passe
        </label>
        <input
          id="register-password-confirmation"
          type="password"
          autoComplete="new-password"
          minLength={15}
          maxLength={128}
          value={passwordConfirmation}
          onChange={(event) => setPasswordConfirmation(event.target.value)}
          disabled={isSubmitting}
          required
        />
      </div>

      {error && (
        <p className="login-form__error" role="alert">
          {error}
        </p>
      )}

      <button type="submit" disabled={isSubmitting}>
        {isSubmitting ? "Création en cours..." : "Créer mon compte"}
      </button>
    </form>
  );
}

export default RegisterForm;