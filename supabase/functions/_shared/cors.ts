export const corsHeaders: Record<string, string> = {
  "Access-Control-Allow-Origin": "*",
  // `*` allows all headers; `authorization` is listed explicitly because the
  // CORS spec excludes Authorization from the wildcard.
  "Access-Control-Allow-Headers": "authorization, *",
  "Access-Control-Allow-Methods": "*",
  "Access-Control-Max-Age": "86400",
};

export function handleCorsPreflight(req: Request): Response | null {
  if (req.method === "OPTIONS") {
    return new Response(null, { status: 204, headers: corsHeaders });
  }
  return null;
}
