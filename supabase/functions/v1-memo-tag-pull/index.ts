import "jsr:@supabase/functions-js/edge-runtime.d.ts";
import { supabaseAdmin } from "../_shared/supabase-admin.ts";
import { MemoTagRepository } from "../_shared/memo-tag-repository.ts";
import { MemoTag } from "../_shared/entity/memo-tag.ts";
import { MemoTagPullRequestV1 } from "./entity/memo-tag-pull-request-v1.ts";
import { MemoTagV1 } from "../v1-memo-tag-push/entity/memo-tag-v1.ts";

const memoTagRepository = new MemoTagRepository(supabaseAdmin);

function toMemoTagV1(memoTag: MemoTag): MemoTagV1 {
  return {
    memoId: memoTag.memoId,
    tagId: memoTag.tagId,
    isMemoTag: memoTag.isMemoTag,
    updatedAt: memoTag.updatedAt,
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

    const { updatedAt }: MemoTagPullRequestV1 = await req.json();
    const memoTagList = await memoTagRepository.getUpdatedAfter(user.id, updatedAt);

    return new Response(JSON.stringify(memoTagList.map(toMemoTagV1)), {
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
