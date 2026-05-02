import "jsr:@supabase/functions-js/edge-runtime.d.ts";
import { supabaseAdmin } from "../_shared/supabase-admin.ts";
import { MemoRepository } from "../_shared/memo-repository.ts";
import { Memo } from "../_shared/entity/memo.ts";
import { corsHeaders, handleCorsPreflight } from "../_shared/cors.ts";
import { MemoPullRequestV2 } from "./entity/memo-pull-request-v2.ts";
import { MemoV2 } from "../v2-memo-push/entity/memo-v2.ts";

const memoRepository = new MemoRepository(supabaseAdmin);

function toMemoV2(memo: Memo): MemoV2 {
  return {
    id: memo.id,
    detail: {
      title: memo.detail.title,
      description: memo.detail.description,
      isAllDay: memo.detail.isAllDay,
      start: memo.detail.start,
      endInclusive: memo.detail.endInclusive,
      color: memo.detail.color,
    },
    primaryTag: memo.primaryTag,
    isFinished: memo.isFinished,
    isDeleted: memo.isDeleted,
    updatedAt: memo.updatedAt,
    createdAt: memo.createdAt,
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

    const { updatedAt }: MemoPullRequestV2 = await req.json();
    const memoList = await memoRepository.getUpdatedAfter(user.id, updatedAt);

    return new Response(JSON.stringify(memoList.map(toMemoV2)), {
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
