import { GoogleTokenResponse } from "./entity/google-token-response.ts";
import { GoogleUserInfo } from "./entity/google-user-info.ts";

export class GoogleRepository {
  private readonly clientSecrets: Record<string, string>;

  constructor() {
    const secrets = Deno.env.get("GOOGLE_CLIENT_SECRETS");
    if (!secrets) {
      throw new Error("GOOGLE_CLIENT_SECRETS not found");
    }
    this.clientSecrets = JSON.parse(secrets);
  }

  async exchangeCode(
    clientId: string,
    code: string,
    redirectUri: string,
  ): Promise<GoogleTokenResponse> {
    const clientSecret = this.clientSecrets[clientId];
    if (!clientSecret) {
      throw new Error(`No client secret found for clientId: ${clientId}`);
    }

    const response = await fetch("https://oauth2.googleapis.com/token", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: new URLSearchParams({
        code,
        client_id: clientId,
        client_secret: clientSecret,
        redirect_uri: redirectUri,
        grant_type: "authorization_code",
      }),
    });

    if (!response.ok) {
      const error = await response.text();
      throw new Error(`Google token exchange failed: ${error}`);
    }

    return response.json();
  }

  decodeIdToken(idToken: string): GoogleUserInfo {
    const payload = JSON.parse(atob(idToken.split(".")[1]));
    return {
      email: payload.email,
      picture: payload.picture,
    };
  }

  async fetchUserInfo(accessToken: string): Promise<GoogleUserInfo> {
    const response = await fetch(
      "https://www.googleapis.com/oauth2/v2/userinfo",
      { headers: { Authorization: `Bearer ${accessToken}` } },
    );

    if (!response.ok) {
      throw new Error("Failed to fetch Google user info");
    }

    return response.json();
  }
}
