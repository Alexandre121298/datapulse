import type { ReactNode } from "react";
import "./AuthLayout.css";

interface AuthLayoutProps {
  children: ReactNode;
}

function AuthLayout({ children }: AuthLayoutProps) {
  return (
    <div className="auth-shell">
      <main className="auth-layout">
        <section
          className="auth-layout__story"
          aria-label="Présentation de DataPulse"
        >
          <div className="auth-layout__brand">
            <span className="auth-layout__logo" aria-hidden="true">
              DP
            </span>
            <span>DataPulse</span>
          </div>

          <div className="auth-layout__introduction">
            <p className="auth-layout__eyebrow">
              VOTRE ESPACE MÉTÉO
            </p>

            <p className="auth-layout__headline">
              Vos villes.
              <br />
              Au fil du ciel.
            </p>

            <p className="auth-layout__description">
              Retrouvez les conditions météo des lieux qui comptent
              pour vous, dans votre espace personnel.
            </p>
          </div>

          <div className="auth-layout__illustration" aria-hidden="true">
            <span className="auth-layout__sun" />
            <span className="auth-layout__cloud" />
          </div>
        </section>

        <section
          className="auth-layout__content"
          aria-label="Accès à votre compte"
        >
          <div className="auth-layout__form-container">
            {children}
          </div>
        </section>
      </main>
    </div>
  );
}

export default AuthLayout;