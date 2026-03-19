import "jsr:@supabase/functions-js/edge-runtime.d.ts";
import { supabaseAdmin } from "../_shared/supabase-admin.ts";
import { MemoRepository } from "../_shared/memo-repository.ts";
import { Memo } from "../_shared/entity/memo.ts";
import { MemoV1 } from "./entity/memo-v1.ts";

const memoRepository = new MemoRepository(supabaseAdmin);

function toMemo(v1: MemoV1): Memo {
  return {
    id: v1.id,
    detail: {
      title: v1.detail.title,
      description: v1.detail.description,
      isAllDay: v1.detail.isAllDay,
      start: v1.detail.start,
      endInclusive: v1.detail.endInclusive,
      color: v1.detail.color,
    },
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

    const { memoList }: { memoList: MemoV1[] } = await req.json();
    await memoRepository.upsertIfNewer(user.id, memoList.map(toMemo));

    return new Response(null, { status: 204 });
  } catch (error) {
    return new Response(JSON.stringify({ error: error.message }), {
      status: 400,
      headers: { "Content-Type": "application/json" },
    });
  }
});
