import "jsr:@supabase/functions-js/edge-runtime.d.ts";
import { supabaseAdmin } from "../_shared/supabase-admin.ts";
import { MemoTagRepository } from "../_shared/memo-tag-repository.ts";
import { MemoTag } from "../_shared/entity/memo-tag.ts";
import { MemoTagV1 } from "./entity/memo-tag-v1.ts";

const memoTagRepository = new MemoTagRepository(supabaseAdmin);

function toMemoTag(v1: MemoTagV1): MemoTag {
  return {
    memoId: v1.memoId,
    tagId: v1.tagId,
    isMemoTag: v1.isMemoTag,
    updatedAt: v1.updatedAt,
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

    const { memoTagList }: { memoTagList: MemoTagV1[] } = await req.json();
    await memoTagRepository.upsertIfNewer(user.id, memoTagList.map(toMemoTag));

    return new Response(null, { status: 204 });
  } catch (error) {
    return new Response(JSON.stringify({ error: error.message }), {
      status: 400,
      headers: { "Content-Type": "application/json" },
    });
  }
});
