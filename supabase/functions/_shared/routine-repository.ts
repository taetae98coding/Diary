import { SupabaseClient } from "jsr:@supabase/supabase-js@2";
import { Routine } from "./entity/routine.ts";

export class RoutineRepository {
  constructor(private readonly supabase: SupabaseClient) {}

  async upsertIfNewer(accountId: string, routineList: Routine[]): Promise<void> {
    for (const routine of routineList) {
      await this.upsertOneIfNewer(accountId, routine);
    }
  }

  private async upsertOneIfNewer(
    accountId: string,
    routine: Routine,
  ): Promise<void> {
    const { data: existing } = await this.supabase
      .from("routine")
      .select("updated_at")
      .eq("id", routine.id)
      .single();

    if (existing && existing.updated_at > routine.updatedAt) {
      return;
    }

    const routineRow = {
      id: routine.id,
      title: routine.title,
      description: routine.description,
      start: routine.start,
      end_inclusive: routine.endInclusive,
      color: routine.color,
      r_rules: routine.rRules,
      r_dates: routine.rDates,
      ex_dates: routine.exDates,
      routine_count: routine.routineCount,
      is_finished: routine.isFinished,
      is_deleted: routine.isDeleted,
      updated_at: routine.updatedAt,
      created_at: routine.createdAt,
    };

    const { error: routineError } = await this.supabase
      .from("routine")
      .upsert(routineRow, { onConflict: "id" });

    if (routineError) {
      throw new Error(`Routine upsert failed: ${routineError.message}`);
    }

    const { error: accountRoutineError } = await this.supabase
      .from("account_routine")
      .upsert(
        { account_id: accountId, routine_id: routine.id },
        { onConflict: "account_id,routine_id" },
      );

    if (accountRoutineError) {
      throw new Error(`AccountRoutine upsert failed: ${accountRoutineError.message}`);
    }
  }

  async getUpdatedAfter(
    accountId: string,
    updatedAt: number,
  ): Promise<Routine[]> {
    const { data, error } = await this.supabase
      .from("routine")
      .select("*, account_routine!inner(account_id)")
      .eq("account_routine.account_id", accountId)
      .gt("updated_at", updatedAt)
      .order("updated_at", { ascending: true })
      .limit(100);

    if (error) {
      throw new Error(`Routine pull failed: ${error.message}`);
    }

    return (data ?? []).map((row: any) => ({
      id: row.id,
      title: row.title,
      description: row.description,
      start: row.start,
      endInclusive: row.end_inclusive,
      color: row.color,
      rRules: row.r_rules,
      rDates: row.r_dates,
      exDates: row.ex_dates,
      routineCount: row.routine_count,
      isFinished: row.is_finished,
      isDeleted: row.is_deleted,
      updatedAt: row.updated_at,
      createdAt: row.created_at,
    }));
  }
}
