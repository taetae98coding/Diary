import "jsr:@supabase/functions-js/edge-runtime.d.ts";
import { supabaseAdmin } from "../_shared/supabase-admin.ts";
import { RoutineRepository } from "../_shared/routine-repository.ts";
import { Routine } from "../_shared/entity/routine.ts";
import { RoutinePullRequestV1 } from "./entity/routine-pull-request-v1.ts";
import { RoutineV1 } from "../v1-routine-push/entity/routine-v1.ts";

const routineRepository = new RoutineRepository(supabaseAdmin);

function toRoutineV1(routine: Routine): RoutineV1 {
  return {
    id: routine.id,
    title: routine.title,
    description: routine.description,
    start: routine.start,
    endInclusive: routine.endInclusive,
    color: routine.color,
    rRules: routine.rRules,
    rDates: routine.rDates,
    exDates: routine.exDates,
    routineCount: routine.routineCount,
    isFinished: routine.isFinished,
    isDeleted: routine.isDeleted,
    updatedAt: routine.updatedAt,
    createdAt: routine.createdAt,
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

    const { updatedAt }: RoutinePullRequestV1 = await req.json();
    const routineList = await routineRepository.getUpdatedAfter(user.id, updatedAt);

    return new Response(JSON.stringify(routineList.map(toRoutineV1)), {
      status: 200,
      headers: { "Content-Type": "application/json" },
    });
  } catch (error) {
    return new Response(JSON.stringify({ error: error.message }), {
      status: 400,
      headers: { "Content-Type": "application/json" },
    });
  }
});
