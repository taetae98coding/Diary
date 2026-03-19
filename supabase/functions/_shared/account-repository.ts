import { SupabaseClient } from "jsr:@supabase/supabase-js@2";
import { Account } from "./entity/account.ts";

export class AccountRepository {
  constructor(private readonly supabase: SupabaseClient) {}

  async upsert(email: string, profileImage: string | null): Promise<Account> {
    const now = Date.now();

    const { data: existing } = await this.supabase
      .from("account")
      .select()
      .eq("email", email)
      .single();

    if (existing) {
      const { data, error } = await this.supabase
        .from("account")
        .update({ profile_image: profileImage, updated_at: now })
        .eq("email", email)
        .select()
        .single();

      if (error) throw new Error(`Account update failed: ${error.message}`);
      return data as Account;
    }

    const { data, error } = await this.supabase
      .from("account")
      .insert({ email, profile_image: profileImage, updated_at: now, created_at: now })
      .select()
      .single();

    if (error) throw new Error(`Account insert failed: ${error.message}`);
    return data as Account;
  }
}
