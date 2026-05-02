import "jsr:@supabase/functions-js/edge-runtime.d.ts";
import { supabaseAdmin } from "../_shared/supabase-admin.ts";
import { AccountRepository } from "../_shared/account-repository.ts";
import { AuthRepository } from "../_shared/auth-repository.ts";
import { corsHeaders, handleCorsPreflight } from "../_shared/cors.ts";
import { SessionGoogleIdTokenRequestV1 } from "./entity/session-google-id-token-request-v1.ts";
import { SessionGoogleIdTokenResponseV1 } from "./entity/session-google-id-token-response-v1.ts";

const accountRepository = new AccountRepository(supabaseAdmin);
const authRepository = new AuthRepository(supabaseAdmin);

Deno.serve(async (req: Request) => {
  const preflight = handleCorsPreflight(req);
  if (preflight) return preflight;

  if (req.method !== "POST") {
    return new Response(JSON.stringify({ error: "Method not allowed" }), {
      status: 405,
      headers: { "Content-Type": "application/json", ...corsHeaders },
    });
  }

  try {
    const { idToken }: SessionGoogleIdTokenRequestV1 = await req.json();

    const payload = decodeIdTokenPayload(idToken);
    const email = payload.email as string;
    const picture = (payload.picture as string) ?? null;

    const account = await accountRepository.upsert(email, picture);
    await authRepository.ensureUser(account.id, account.email);
    const signInData = await authRepository.signInWithGoogleIdToken(idToken);

    const response: SessionGoogleIdTokenResponseV1 = {
      accessToken: signInData.access_token,
      refreshToken: signInData.refresh_token,
      account: {
        id: account.id,
        email: account.email,
        profileImage: account.profile_image,
      },
    };

    return new Response(JSON.stringify(response), {
      status: 200,
      headers: { "Content-Type": "application/json", ...corsHeaders },
    });
  } catch (error) {
    return new Response(JSON.stringify({ error: error.message }), {
      status: 400,
      headers: { "Content-Type": "application/json", ...corsHeaders },
    });
  }
});

function decodeIdTokenPayload(idToken: string): Record<string, unknown> {
  const parts = idToken.split(".");
  if (parts.length !== 3) {
    throw new Error("Invalid ID token format");
  }

  let payload = parts[1].replace(/-/g, "+").replace(/_/g, "/");
  const pad = payload.length % 4;
  if (pad) {
    payload += "=".repeat(4 - pad);
  }

  const decoded = atob(payload);
  return JSON.parse(decoded);
}
