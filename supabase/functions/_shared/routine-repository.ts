import { SupabaseClient } from "jsr:@supabase/supabase-js@2";
import { Routine } from "./entity/routine.ts";
import { RoutineRRule } from "./entity/routine-rrule.ts";

function parseRRules(value: string | null): RoutineRRule[] {
  if (!value) return [];
  try {
    return JSON.parse(value) as RoutineRRule[];
  } catch (_) {
    return [];
  }
}

function parseStringList(value: string | null): string[] {
  if (!value) return [];
  try {
    return JSON.parse(value) as string[];
  } catch (_) {
    return [];
  }
}

function rowToRoutine(row: any): Routine {
  return {
    id: row.id,
    detail: {
      title: row.title,
      description: row.description,
      start: row.start,
      endInclusive: row.end_inclusive,
      color: row.color,
      routineCount: row.routine_count,
    },
    rRules: parseRRules(row.r_rules),
    rDates: parseStringList(row.r_dates),
    exDates: parseStringList(row.ex_dates),
    isCalendarVisible: row.is_calendar_visible ?? true,
    isFinished: row.is_finished,
    isDeleted: row.is_deleted,
    updatedAt: row.updated_at,
    createdAt: row.created_at,
  };
}

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
      title: routine.detail.title,
      description: routine.detail.description,
      start: routine.detail.start,
      end_inclusive: routine.detail.endInclusive,
      color: routine.detail.color,
      routine_count: routine.detail.routineCount,
      r_rules: JSON.stringify(routine.rRules),
      r_dates: JSON.stringify(routine.rDates),
      ex_dates: JSON.stringify(routine.exDates),
      is_calendar_visible: routine.isCalendarVisible,
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
      throw new Error(
        `AccountRoutine upsert failed: ${accountRoutineError.message}`,
      );
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

    return (data ?? []).map(rowToRoutine);
  }
}
