import { SupabaseClient } from "jsr:@supabase/supabase-js@2";
import { Tag } from "./entity/tag.ts";

export class TagRepository {
  constructor(private readonly supabase: SupabaseClient) {}

  async upsertIfNewer(accountId: string, tagList: Tag[]): Promise<void> {
    for (const tag of tagList) {
      await this.upsertOneIfNewer(accountId, tag);
    }
  }

  private async upsertOneIfNewer(
    accountId: string,
    tag: Tag,
  ): Promise<void> {
    const { data: existing } = await this.supabase
      .from("tag")
      .select("updated_at")
      .eq("id", tag.id)
      .single();

    if (existing && existing.updated_at > tag.updatedAt) {
      return;
    }

    const tagRow = {
      id: tag.id,
      title: tag.detail.title,
      description: tag.detail.description,
      color: tag.detail.color,
      is_finished: tag.isFinished,
      is_deleted: tag.isDeleted,
      updated_at: tag.updatedAt,
      created_at: tag.createdAt,
    };

    const { error: tagError } = await this.supabase
      .from("tag")
      .upsert(tagRow, { onConflict: "id" });

    if (tagError) {
      throw new Error(`Tag upsert failed: ${tagError.message}`);
    }

    const { error: accountTagError } = await this.supabase
      .from("account_tag")
      .upsert(
        { account_id: accountId, tag_id: tag.id },
        { onConflict: "account_id,tag_id" },
      );

    if (accountTagError) {
      throw new Error(`AccountTag upsert failed: ${accountTagError.message}`);
    }
  }

  async getUpdatedAfter(
    accountId: string,
    updatedAt: number,
  ): Promise<Tag[]> {
    const { data, error } = await this.supabase
      .from("tag")
      .select("*, account_tag!inner(account_id)")
      .eq("account_tag.account_id", accountId)
      .gt("updated_at", updatedAt)
      .order("updated_at", { ascending: true })
      .limit(100);

    if (error) {
      throw new Error(`Tag pull failed: ${error.message}`);
    }

    return (data ?? []).map((row: any) => ({
      id: row.id,
      detail: {
        title: row.title,
        description: row.description,
        color: row.color,
      },
      isFinished: row.is_finished,
      isDeleted: row.is_deleted,
      updatedAt: row.updated_at,
      createdAt: row.created_at,
    }));
  }
}
