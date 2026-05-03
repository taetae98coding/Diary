# Supabase 운영 지침

## DB 접근 정책

- 모든 DB 접근은 Edge Function(`service_role`)을 경유한다. 클라이언트(supabase-js 등)에서 테이블에 직접 접근하지 않는다.
- 모든 public 테이블에 `service_role only` RLS 정책을 둔다. `service_role`은 `BYPASSRLS`로 정책을 거치지 않고 통과하며, anon/authenticated는 정책에 의해 차단된다.
- 이벤트 트리거 `ensure_rls`(`public.rls_auto_enable()`)가 신규 테이블 생성 시 **RLS enable + 정책 추가**를 자동 수행한다. 따라서 새 테이블 마이그레이션에서 별도 RLS/정책 작업은 불필요하다.
- `rls_auto_enable()`은 `SECURITY DEFINER` 함수이며 `PUBLIC`/`anon`/`authenticated`의 `EXECUTE` 권한이 `REVOKE`되어 있어 RPC로 호출할 수 없다. 신규 `SECURITY DEFINER` 함수 추가 시 동일 패턴(EXECUTE REVOKE)을 따른다.

정책 정의:

```sql
CREATE POLICY "service_role only" ON public.<table> FOR ALL TO public
  USING ((select auth.role()) = 'service_role')
  WITH CHECK ((select auth.role()) = 'service_role');
```

`auth.role()`을 `(select ...)`로 감싸면 row마다 재평가되지 않고 쿼리당 1회만 평가된다 (initplan 캐싱). Supabase Advisor의 `auth_rls_initplan` 경고를 피하기 위함.

## Dev / Real 환경 비교

Dev와 Real 두 Supabase 프로젝트의 정합성을 점검할 때 따르는 절차.

### 원칙

- **마이그레이션 히스토리는 비교하지 않는다.** Dev는 개발 중 시행착오·롤백·재정렬이 잦아 1:1 대조가 무의미하다.
- **현재 스키마 객체 자체(결과물)를 비교한다.** Real에 누락된 객체 / 타입 불일치만 추려내는 것이 목표.

### 비교 대상

| 항목 | 비교 도구 |
|---|---|
| 테이블 / 컬럼 / PK / FK | `mcp__supabase__list_tables` (verbose) |
| RLS Policy | `pg_policies` |
| Index | `pg_indexes` |
| Trigger | `information_schema.triggers` |
| Function | `pg_proc` + `pg_namespace` |
| Unique / Check 제약 | `information_schema.table_constraints` |
| Sequence / default | `pg_attribute` + `pg_attrdef` |
| Edge Function 본문 | `mcp__supabase__get_edge_function` (sha256 비교 후 본문 diff) |

### 차이 발견 시 처리 방향

- **Real에만 누락**: Dev 정의를 정본으로 보고 Real에 마이그레이션 적용.
- **Dev에만 누락**: 실험성 객체일 가능성. Dev에서 정리 후 적용 여부 결정.
- **양쪽에 다른 정의**: 코드(클라이언트/Edge Function) 사용 형태를 기준으로 어느 쪽이 맞는지 판단.
