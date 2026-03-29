import { SupabaseClient } from "jsr:@supabase/supabase-js@2";
import { Memo } from "./entity/memo.ts";

function stripTimezone(value: string | null): string | null {
  if (value == null) return null;
  // "2026-03-18T00:00:00+00:00" → "2026-03-18T00:00:00"
  return value.replace(/[+-]\d{2}:\d{2}$/, "");
}

export class MemoRepository {
  constructor(private readonly supabase: SupabaseClient) {}

  async findById(id: string): Promise<Memo | null> {
    const { data } = await this.supabase
      .from("memo")
      .select("*")
      .eq("id", id)
      .single();

    if (!data) return null;

    return {
      id: data.id,
      detail: {
        title: data.title,
        description: data.description,
        isAllDay: data.is_all_day,
        start: stripTimezone(data.start),
        endInclusive: stripTimezone(data.end_inclusive),
        color: data.color,
      },
      primaryTag: data.primary_tag,
      isFinished: data.is_finished,
      isDeleted: data.is_deleted,
      updatedAt: data.updated_at,
      createdAt: data.created_at,
    };
  }

  async upsertIfNewer(accountId: string, memoList: Memo[]): Promise<void> {
    for (const memo of memoList) {
      await this.upsertOneIfNewer(accountId, memo);
    }
  }

  private async upsertOneIfNewer(
    accountId: string,
    memo: Memo,
  ): Promise<void> {
    const { data: existing } = await this.supabase
      .from("memo")
      .select("updated_at")
      .eq("id", memo.id)
      .single();

    // Server has newer data — conflict, treat as success (skip)
    if (existing && existing.updated_at > memo.updatedAt) {
      return;
    }

    const memoRow = {
      id: memo.id,
      title: memo.detail.title,
      description: memo.detail.description,
      is_all_day: memo.detail.isAllDay,
      start: memo.detail.start,
      end_inclusive: memo.detail.endInclusive,
      color: memo.detail.color,
      primary_tag: memo.primaryTag,
      is_finished: memo.isFinished,
      is_deleted: memo.isDeleted,
      updated_at: memo.updatedAt,
      created_at: memo.createdAt,
    };

    const { error: memoError } = await this.supabase
      .from("memo")
      .upsert(memoRow, { onConflict: "id" });

    if (memoError) {
      throw new Error(`Memo upsert failed: ${memoError.message}`);
    }

    const { error: accountMemoError } = await this.supabase
      .from("account_memo")
      .upsert(
        { account_id: accountId, memo_id: memo.id },
        { onConflict: "account_id,memo_id" },
      );

    if (accountMemoError) {
      throw new Error(`AccountMemo upsert failed: ${accountMemoError.message}`);
    }
  }

  async getUpdatedAfter(
    accountId: string,
    updatedAt: number,
  ): Promise<Memo[]> {
    const { data, error } = await this.supabase
      .from("memo")
      .select("*, account_memo!inner(account_id)")
      .eq("account_memo.account_id", accountId)
      .gt("updated_at", updatedAt)
      .order("updated_at", { ascending: true })
      .limit(100);

    if (error) {
      throw new Error(`Memo pull failed: ${error.message}`);
    }

    return (data ?? []).map((row: any) => ({
      id: row.id,
      detail: {
        title: row.title,
        description: row.description,
        isAllDay: row.is_all_day,
        start: stripTimezone(row.start),
        endInclusive: stripTimezone(row.end_inclusive),
        color: row.color,
      },
      primaryTag: row.primary_tag,
      isFinished: row.is_finished,
      isDeleted: row.is_deleted,
      updatedAt: row.updated_at,
      createdAt: row.created_at,
    }));
  }
}
