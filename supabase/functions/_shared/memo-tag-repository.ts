import { SupabaseClient } from "jsr:@supabase/supabase-js@2";
import { MemoTag } from "./entity/memo-tag.ts";

export class MemoTagRepository {
  constructor(private readonly supabase: SupabaseClient) {}

  async upsertIfNewer(accountId: string, memoTagList: MemoTag[]): Promise<void> {
    for (const memoTag of memoTagList) {
      await this.upsertOneIfNewer(accountId, memoTag);
    }
  }

  private async upsertOneIfNewer(
    accountId: string,
    memoTag: MemoTag,
  ): Promise<void> {
    const { data: existing } = await this.supabase
      .from("memo_tag")
      .select("updated_at")
      .eq("memo_id", memoTag.memoId)
      .eq("tag_id", memoTag.tagId)
      .single();

    if (existing && existing.updated_at > memoTag.updatedAt) {
      return;
    }

    const memoTagRow = {
      memo_id: memoTag.memoId,
      tag_id: memoTag.tagId,
      is_memo_tag: memoTag.isMemoTag,
      updated_at: memoTag.updatedAt,
    };

    const { error } = await this.supabase
      .from("memo_tag")
      .upsert(memoTagRow, { onConflict: "memo_id,tag_id" });

    if (error) {
      throw new Error(`MemoTag upsert failed: ${error.message}`);
    }
  }

  async getUpdatedAfter(
    accountId: string,
    updatedAt: number,
  ): Promise<MemoTag[]> {
    const { data, error } = await this.supabase
      .from("memo_tag")
      .select("*, memo!inner(account_memo!inner(account_id))")
      .eq("memo.account_memo.account_id", accountId)
      .gt("updated_at", updatedAt)
      .order("updated_at", { ascending: true })
      .limit(100);

    if (error) {
      throw new Error(`MemoTag pull failed: ${error.message}`);
    }

    return (data ?? []).map((row: any) => ({
      memoId: row.memo_id,
      tagId: row.tag_id,
      isMemoTag: row.is_memo_tag,
      updatedAt: row.updated_at,
    }));
  }
}
