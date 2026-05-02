import "jsr:@supabase/functions-js/edge-runtime.d.ts";
import { supabaseAdmin } from "../_shared/supabase-admin.ts";
import { TagRepository } from "../_shared/tag-repository.ts";
import { Tag } from "../_shared/entity/tag.ts";
import { corsHeaders, handleCorsPreflight } from "../_shared/cors.ts";
import { TagPullRequestV1 } from "./entity/tag-pull-request-v1.ts";
import { TagV1 } from "../v1-tag-push/entity/tag-v1.ts";

const tagRepository = new TagRepository(supabaseAdmin);

function toTagV1(tag: Tag): TagV1 {
  return {
    id: tag.id,
    detail: {
      title: tag.detail.title,
      description: tag.detail.description,
      color: tag.detail.color,
    },
    isFinished: tag.isFinished,
    isDeleted: tag.isDeleted,
    updatedAt: tag.updatedAt,
    createdAt: tag.createdAt,
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

    const { updatedAt }: TagPullRequestV1 = await req.json();
    const tagList = await tagRepository.getUpdatedAfter(user.id, updatedAt);

    return new Response(JSON.stringify(tagList.map(toTagV1)), {
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
