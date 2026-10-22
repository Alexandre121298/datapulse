import { useState, type FormEvent } from "react";
import "./LoginForm.css";

interface LoginFormProps {
  onLogin: (email: string, password: string) => Promise<void>;
}

function LoginForm({ onLogin }: LoginFormProps) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setIsSubmitting(true);
    setError(null);

    try {
      await onLogin(email.trim(), password);
    } catch (err) {
      setError(
        err instanceof Error
          ? err.message
          : "Impossible de se connecter."
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
      <h1>Se connecter</h1>
      <p>Connectez-vous pour retrouver vos villes.</p>

      <div className="login-form__field">
        <label htmlFor="login-email">Email</label>
        <input
          id="login-email"
          type="email"
          autoComplete="username"
          value={email}
          onChange={(event) => setEmail(event.target.value)}
          disabled={isSubmitting}
          required
        />
      </div>

      <div className="login-form__field">
        <label htmlFor="login-password">Mot de passe</label>
        <input
          id="login-password"
          type="password"
          autoComplete="current-password"
          value={password}
          onChange={(event) => setPassword(event.target.value)}
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
        {isSubmitting ? "Connexion en cours..." : "Se connecter"}
      </button>
    </form>
  );
}

export default LoginForm;