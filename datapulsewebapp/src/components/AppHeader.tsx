import "./AppHeader.css";

interface AppHeaderProps {
  email: string;
  isLoggingOut: boolean;
  onLogout: () => Promise<void>;
}

function AppHeader({
  email,
  isLoggingOut,
  onLogout,
}: AppHeaderProps) {
  return (
    <header className="app-header">
      <div className="app-header__inner">
        <a
          className="app-header__brand"
          href="/"
          aria-label="DataPulse, accueil"
        >
          <span className="app-header__brand-mark" aria-hidden="true">
            DP
          </span>

          <span>
            <strong className="app-header__brand-name">DataPulse</strong>
            <span className="app-header__tagline">
              Votre météo personnelle
            </span>
          </span>
        </a>

        <div className="app-header__account">
          <div className="app-header__identity">
            <span className="app-header__account-label">
              Compte connecté
            </span>
            <span className="app-header__email" title={email}>
              {email}
            </span>
          </div>

          <button
            className="app-header__logout"
            type="button"
            onClick={() => void onLogout()}
            disabled={isLoggingOut}
          >
            {isLoggingOut ? "Déconnexion..." : "Se déconnecter"}
          </button>
        </div>
      </div>
    </header>
  );
}

export default AppHeader;