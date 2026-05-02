import "jsr:@supabase/functions-js/edge-runtime.d.ts";
import { supabaseAdmin } from "../_shared/supabase-admin.ts";
import { RoutineRepository } from "../_shared/routine-repository.ts";
import { Routine } from "../_shared/entity/routine.ts";
import { corsHeaders, handleCorsPreflight } from "../_shared/cors.ts";
import { RoutinePullRequestV1 } from "./entity/routine-pull-request-v1.ts";
import { RoutineV1 } from "../v1-routine-push/entity/routine-v1.ts";

const routineRepository = new RoutineRepository(supabaseAdmin);

function toRoutineV1(routine: Routine): RoutineV1 {
  return {
    id: routine.id,
    detail: {
      title: routine.detail.title,
      description: routine.detail.description,
      start: routine.detail.start,
      endInclusive: routine.detail.endInclusive,
      color: routine.detail.color,
      routineCount: routine.detail.routineCount,
    },
    rRules: routine.rRules.map((rule) => ({
      diaryByDay: {
        days: rule.diaryByDay.days,
        ordinal: rule.diaryByDay.ordinal,
      },
      byMonthDay: rule.byMonthDay,
    })),
    rDates: routine.rDates,
    exDates: routine.exDates,
    isCalendarVisible: routine.isCalendarVisible,
    isFinished: routine.isFinished,
    isDeleted: routine.isDeleted,
    updatedAt: routine.updatedAt,
    createdAt: routine.createdAt,
  };
}

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
    const authorization = req.headers.get("Authorization");
    if (!authorization) {
      return new Response(JSON.stringify({ error: "Unauthorized" }), {
        status: 401,
        headers: { "Content-Type": "application/json", ...corsHeaders },
      });
    }

    const { data: { user }, error: authError } = await supabaseAdmin.auth.getUser(
      authorization.replace("Bearer ", ""),
    );

    if (authError || !user) {
      return new Response(JSON.stringify({ error: "Unauthorized" }), {
        status: 401,
        headers: { "Content-Type": "application/json", ...corsHeaders },
      });
    }

    const { updatedAt }: RoutinePullRequestV1 = await req.json();
    const routineList = await routineRepository.getUpdatedAfter(user.id, updatedAt);

    return new Response(JSON.stringify(routineList.map(toRoutineV1)), {
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
