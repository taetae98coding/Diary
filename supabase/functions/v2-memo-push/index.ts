import "jsr:@supabase/functions-js/edge-runtime.d.ts";
import { supabaseAdmin } from "../_shared/supabase-admin.ts";
import { MemoRepository } from "../_shared/memo-repository.ts";
import { Memo } from "../_shared/entity/memo.ts";
import { MemoV2 } from "./entity/memo-v2.ts";

const memoRepository = new MemoRepository(supabaseAdmin);

function toMemo(v2: MemoV2): Memo {
  return {
    id: v2.id,
    detail: {
      title: v2.detail.title,
      description: v2.detail.description,
      isAllDay: v2.detail.isAllDay,
      start: v2.detail.start,
      endInclusive: v2.detail.endInclusive,
      color: v2.detail.color,
    },
    primaryTag: v2.primaryTag,
    isFinished: v2.isFinished,
    isDeleted: v2.isDeleted,
    updatedAt: v2.updatedAt,
    createdAt: v2.createdAt,
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

    const { memoList }: { memoList: MemoV2[] } = await req.json();
    await memoRepository.upsertIfNewer(user.id, memoList.map(toMemo));

    return new Response(null, { status: 204 });
  } catch (error) {
    return new Response(JSON.stringify({ error: error.message }), {
      status: 400,
      headers: { "Content-Type": "application/json" },
    });
  }
});
