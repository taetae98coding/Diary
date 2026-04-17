import "jsr:@supabase/functions-js/edge-runtime.d.ts";
import { supabaseAdmin } from "../_shared/supabase-admin.ts";
import { RoutineRepository } from "../_shared/routine-repository.ts";
import { Routine } from "../_shared/entity/routine.ts";
import { RoutineV1 } from "./entity/routine-v1.ts";

const routineRepository = new RoutineRepository(supabaseAdmin);

function toRoutine(v1: RoutineV1): Routine {
  return {
    id: v1.id,
    detail: {
      title: v1.detail.title,
      description: v1.detail.description,
      start: v1.detail.start,
      endInclusive: v1.detail.endInclusive,
      color: v1.detail.color,
      routineCount: v1.detail.routineCount,
    },
    rRules: v1.rRules.map((rule) => ({
      diaryByDay: {
        days: rule.diaryByDay.days,
        ordinal: rule.diaryByDay.ordinal,
      },
      byMonthDay: rule.byMonthDay,
    })),
    rDates: v1.rDates,
    exDates: v1.exDates,
    isFinished: v1.isFinished,
    isDeleted: v1.isDeleted,
    updatedAt: v1.updatedAt,
    createdAt: v1.createdAt,
  };
}

Deno.serve(async (req: Request) => {
  if (req.method !== "POST") {
    return new Response(JSON.stringify({ error: "Method not allowed" }), {
      status: 405,
      headers: { "Content-Type": "application/json" },
    });
  }

  try {
    const authorization = req.headers.get("Authorization");
    if (!authorization) {
      return new Response(JSON.stringify({ error: "Unauthorized" }), {
        status: 401,
        headers: { "Content-Type": "application/json" },
      });
    }

    const { data: { user }, error: authError } = await supabaseAdmin.auth.getUser(
      authorization.replace("Bearer ", ""),
    );

    if (authError || !user) {
      return new Response(JSON.stringify({ error: "Unauthorized" }), {
        status: 401,
        headers: { "Content-Type": "application/json" },
      });
    }

    const { routineList }: { routineList: RoutineV1[] } = await req.json();
    await routineRepository.upsertIfNewer(user.id, routineList.map(toRoutine));

    return new Response(null, { status: 204 });
  } catch (error) {
    return new Response(JSON.stringify({ error: error.message }), {
      status: 400,
      headers: { "Content-Type": "application/json" },
    });
  }
});
