import type { AuthUser } from "../types/auth";
import { apiFetch, readErrorMessage } from "./apiClient";

export async function getCurrentUser(
  signal?: AbortSignal
): Promise<AuthUser | null> {
  const response = await apiFetch("/api/auth/me", {
    signal,
    cache: "no-store",
  });

  if (response.status === 401) {
    return null;
  }

  if (!response.ok) {
    throw new Error(
      "Impossible de récupérer le compte connecté."
    );
  }

  return (await response.json()) as AuthUser;
}

export async function login(
  email: string,
  password: string
): Promise<AuthUser> {
  const response = await apiFetch("/api/auth/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
    },
    body: new URLSearchParams({
      email,
      password,
    }),
  });

  if (response.status === 401) {
    throw new Error("Email ou mot de passe incorrect.");
  }

  if (!response.ok) {
    throw new Error(
      await readErrorMessage(
        response,
        "Impossible de se connecter."
      )
    );
  }

  const user = await getCurrentUser();

  if (user === null) {
    throw new Error(
      "La session n'a pas pu être établie."
    );
  }

  return user;
}

export async function register(
  email: string,
  password: string
): Promise<AuthUser> {
  const response = await apiFetch("/api/auth/register", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      email,
      password,
    }),
  });

  if (!response.ok) {
    throw new Error(
      await readErrorMessage(
        response,
        "Impossible de créer le compte."
      )
    );
  }

  return (await response.json()) as AuthUser;
}

export async function logout(): Promise<void> {
  const response = await apiFetch("/api/auth/logout", {
    method: "POST",
  });

  if (!response.ok) {
    throw new Error("Impossible de se déconnecter.");
  }
}