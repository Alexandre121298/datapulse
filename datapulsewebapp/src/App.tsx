import { useEffect, useState } from "react";
import LoginForm from "./components/LoginForm";
import TrackedCitiesPage from "./pages/TrackedCitiesPage";
import { getCurrentUser, login, logout } from "./services/authService";
import { SESSION_EXPIRED_EVENT } from "./services/apiClient";
import type { AuthUser } from "./types/auth";
import "./App.css";

function App() {
  const [user, setUser] = useState<AuthUser | null>(null);
  const [isCheckingSession, setIsCheckingSession] = useState(true);
  const [isLoggingOut, setIsLoggingOut] = useState(false);
  const [sessionError, setSessionError] = useState<string | null>(null);

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

  async function handleLogin(email: string, password: string) {
    setSessionError(null);
    const currentUser = await login(email, password);
    setUser(currentUser);
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

        <LoginForm onLogin={handleLogin} />
      </main>
    );
  }

  return (
    <>
      <div className="account-bar">
        <span>Connecté avec {user.email}</span>

        <button
          type="button"
          onClick={() => void handleLogout()}
          disabled={isLoggingOut}
        >
          {isLoggingOut ? "Déconnexion..." : "Se déconnecter"}
        </button>
      </div>

      {sessionError && (
        <p className="session-error" role="alert">
          {sessionError}
        </p>
      )}

      <TrackedCitiesPage key={user.id} />
    </>
  );
}

export default App;