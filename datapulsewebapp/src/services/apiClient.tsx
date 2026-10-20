export const SESSION_EXPIRED_EVENT = "datapulse:session-expired";

interface CsrfResponse {
  headerName: string;
  token: string;
}

export async function apiFetch(
  url: string,
  options: RequestInit = {}
): Promise<Response> {
  const method = (options.method ?? "GET").toUpperCase();
  const headers = new Headers(options.headers);

  const requiresCsrf = !["GET", "HEAD", "OPTIONS"].includes(method);

  if (requiresCsrf) {
    const csrfResponse = await fetch("/api/auth/csrf", {
      credentials: "include",
      cache: "no-store",
      signal: options.signal,
    });

    if (!csrfResponse.ok) {
      throw new Error(
        "Impossible de préparer la requête sécurisée."
      );
    }

    const csrf = (await csrfResponse.json()) as CsrfResponse;

    headers.set(csrf.headerName, csrf.token);
  }

  const response = await fetch(url, {
    ...options,
    method,
    headers,
    credentials: "include",
  });

  if (
    response.status === 401 &&
    !url.startsWith("/api/auth/") &&
    !options.signal?.aborted
  ) {
    window.dispatchEvent(new Event(SESSION_EXPIRED_EVENT));
  }

  return response;
}

export async function readErrorMessage(
  response: Response,
  fallback: string
): Promise<string> {
  try {
    const body = (await response.json()) as {
      message?: unknown;
    };

    return typeof body.message === "string"
      ? body.message
      : fallback;
  } catch {
    return fallback;
  }
}