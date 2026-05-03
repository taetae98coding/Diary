import "jsr:@supabase/functions-js/edge-runtime.d.ts";
import { supabaseAdmin } from "../_shared/supabase-admin.ts";
import { TagRepository } from "../_shared/tag-repository.ts";
import { Tag } from "../_shared/entity/tag.ts";
import { corsHeaders, handleCorsPreflight } from "../_shared/cors.ts";
import { TagV1 } from "./entity/tag-v1.ts";

const tagRepository = new TagRepository(supabaseAdmin);

function toTag(v1: TagV1): Tag {
  return {
    id: v1.id,
    detail: {
      title: v1.detail.title,
      description: v1.detail.description,
      color: v1.detail.color,
    },
    isFinished: v1.isFinished,
    isDeleted: v1.isDeleted,
    updatedAt: v1.updatedAt,
    createdAt: v1.createdAt,
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

    const { tagList }: { tagList: TagV1[] } = await req.json();
    await tagRepository.upsertIfNewer(user.id, tagList.map(toTag));

    return new Response(null, { status: 204, headers: corsHeaders });
  } catch (error) {
    return new Response(JSON.stringify({ error: error.message }), {
      status: 400,
      headers: { "Content-Type": "application/json", ...corsHeaders },
    });
  }
});
