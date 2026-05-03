import "jsr:@supabase/functions-js/edge-runtime.d.ts";
import { supabaseAdmin } from "../_shared/supabase-admin.ts";
import { GoogleRepository } from "../_shared/google-repository.ts";
import { AccountRepository } from "../_shared/account-repository.ts";
import { AuthRepository } from "../_shared/auth-repository.ts";
import { corsHeaders, handleCorsPreflight } from "../_shared/cors.ts";
import { SessionGoogleCodeRequestV1 } from "./entity/session-google-code-request-v1.ts";
import { SessionGoogleCodeResponseV1 } from "./entity/session-google-code-response-v1.ts";

const googleRepository = new GoogleRepository();
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
    const { clientId, code, redirectUri }: SessionGoogleCodeRequestV1 = await req.json();

    const tokenData = await googleRepository.exchangeCode(clientId, code, redirectUri);
    const userInfo = await googleRepository.fetchUserInfo(tokenData.access_token);

    const account = await accountRepository.upsert(userInfo.email, userInfo.picture ?? null);
    await authRepository.ensureUser(account.id, account.email);
    const signInData = await authRepository.signInWithGoogleIdToken(tokenData.id_token);

    const response: SessionGoogleCodeResponseV1 = {
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
