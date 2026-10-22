import { useEffect, useState } from "react";
import LoginForm from "./components/LoginForm";
import RegisterForm from "./components/RegisterForm";
import TrackedCitiesPage from "./pages/TrackedCitiesPage";
import { getCurrentUser, login, logout, register } from "./services/authService";
import { SESSION_EXPIRED_EVENT } from "./services/apiClient";
import type { AuthUser } from "./types/auth";
import AppHeader from "./components/AppHeader";
import "./App.css";

function getGoogleLoginError(): string | null {
  const params = new URLSearchParams(window.location.search);
  const error = params.get("authError");

  switch (error) {
    case "account_exists":
      return (
        "Un compte utilise déjà cette adresse email. " +
        "Connectez-vous avec votre mot de passe."
      );

    case "google":
      return "La connexion avec Google n’a pas abouti. Veuillez réessayer.";

    default:
      return null;
  }
}

function App() {
  const [user, setUser] = useState<AuthUser | null>(null);
  const [isCheckingSession, setIsCheckingSession] = useState(true);
  const [isLoggingOut, setIsLoggingOut] = useState(false);
  const [sessionError, setSessionError] = useState<string | null>(getGoogleLoginError);
  const [isRegistering, setIsRegistering] = useState(false);
  const [registrationMessage, setRegistrationMessage] = useState<string | null>(
    null
  );

  useEffect(() => {
    const controller = new AbortController();

    async function restoreSession() {
      try {
        const currentUser = await getCurrentUser(controller.signal);

        if (!controller.signal.aborted) {
          setUser(currentUser);
        }
      } catch {
        if (!controller.signal.aborted) {
          setSessionError(
            "Impossible de vérifier la session. Vérifiez que l’API est démarrée."
          );
        }
      } finally {
        if (!controller.signal.aborted) {
          setIsCheckingSession(false);
        }
      }
    }

    void restoreSession();

    return () => controller.abort();
  }, []);

  useEffect(() => {
    function handleSessionExpired() {
      setUser(null);
      setSessionError(
        "Votre session n’est plus active. Reconnectez-vous."
      );
    }

    window.addEventListener(
      SESSION_EXPIRED_EVENT,
      handleSessionExpired
    );

    return () => {
      window.removeEventListener(
        SESSION_EXPIRED_EVENT,
        handleSessionExpired
      );
    };
  }, []);

  useEffect(() => {
    const url = new URL(window.location.href);

    if (!url.searchParams.has("authError")) {
      return;
    }

    url.searchParams.delete("authError");

    window.history.replaceState(
      window.history.state,
      "",
      url.pathname + url.search + url.hash
    );
  }, []);

  async function handleLogin(email: string, password: string) {
    setSessionError(null);
    const currentUser = await login(email, password);
    setUser(currentUser);
  }

  async function handleRegister(email: string, password: string) {
    await register(email, password);

    setIsRegistering(false);
    setSessionError(null);
    setRegistrationMessage(
      "Votre compte a été créé. Vous pouvez maintenant vous connecter."
    );
  }

  async function handleLogout() {
    setIsLoggingOut(true);
    setSessionError(null);

    try {
      await logout();
      setUser(null);
    } catch {
      setSessionError(
        "La déconnexion a échoué. Veuillez réessayer."
      );
    } finally {
      setIsLoggingOut(false);
    }
  }

  

  if (isCheckingSession) {
    return (
      <main className="session-status">
        <p role="status">Vérification de la session...</p>
      </main>
    );
  }

  if (user === null) {
    return (
      <main className="auth-page">
        {sessionError && (
          <p className="session-error" role="alert">
            {sessionError}
          </p>
        )}

        {registrationMessage && (
          <p className="registration-message" role="status">
            {registrationMessage}
          </p>
        )}

        {isRegistering ? (
          <RegisterForm onRegister={handleRegister} />
        ) : (
          <LoginForm onLogin={handleLogin} />
        )}

        <a className="google-login" href="/api/oauth2/authorization/google">
          Continuer avec Google
        </a>

        <button
          type="button"
          className="auth-switch"
          onClick={() => {
            setIsRegistering((previous) => !previous);
            setSessionError(null);
            setRegistrationMessage(null);
          }}
        >
          {isRegistering
            ? "J’ai déjà un compte : me connecter"
            : "Créer un compte"}
        </button>
      </main>
    );
  }

  

  return (
    <div className="app-shell">
      <AppHeader
        email={user.email}
        isLoggingOut={isLoggingOut}
        onLogout={handleLogout}
      />

      {sessionError && (
        <p className="session-error app-shell__error" role="alert">
          {sessionError}
        </p>
      )}

      <TrackedCitiesPage key={user.id} />
    </div>
  );
}

export default App;