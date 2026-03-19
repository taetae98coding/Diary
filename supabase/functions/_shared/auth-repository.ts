import { SupabaseClient } from "jsr:@supabase/supabase-js@2";
import { SignInResult } from "./entity/sign-in-result.ts";

export class AuthRepository {
  constructor(private readonly supabase: SupabaseClient) {}

  async ensureUser(id: string, email: string): Promise<void> {
    const { data } = await this.supabase.auth.admin.listUsers();
    const exists = data?.users?.find((u: { email?: string }) => u.email === email);

    if (!exists) {
      await this.supabase.auth.admin.createUser({
        id,
        email,
        email_confirm: true,
      });
    }
  }

  async signInWithGoogleIdToken(idToken: string): Promise<SignInResult> {
    const supabaseUrl = Deno.env.get("SUPABASE_URL")!;
    const serviceRoleKey = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")!;

    const response = await fetch(`${supabaseUrl}/auth/v1/token?grant_type=id_token`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "apikey": serviceRoleKey,
      },
      body: JSON.stringify({
        provider: "google",
        id_token: idToken,
      }),
    });

    if (!response.ok) {
      const error = await response.text();
      throw new Error(`Supabase sign in failed: ${error}`);
    }

    return response.json();
  }
}
