# Routine Spec

## 1. 개요

Routine은 **반복되는 일정**을 표현하는 도메인이다. RFC 5545 (iCalendar) 의 RRULE 표준을 기반으로 모델을 구성한다.

- 단발성 일정은 `Memo`가 담당한다. Routine은 "규칙에 따라 여러 번 발생하는" 이벤트다.
- Routine은 **규칙(RRULE)**, **명시적 포함(RDATE)**, **명시적 제외(EXDATE)** 세 가지로 실제 발생일 집합을 결정한다.
- 하나의 Routine은 **여러 개의 RRULE을 OR 결합**할 수 있다 (`Routine.rRules: List<RoutineRRule>`).

## 2. 설계 원칙

### RFC 5545 호환 구조 (모델 레벨)

`RoutineRRule` 등 도메인 모델은 **RFC 5545의 호환 구조**(필드명, 타입 관례)를 기본으로 따른다. 스펙을 따르는 외부 시스템(iCalendar 파일 등)과의 호환성 확보 및 향후 확장 시 스펙 충돌 방지가 목적이다.

- RFC 의미를 그대로 보존하는 필드는 RFC 표기를 따른다 (예: `byMonthDay`).
- 단, **실제로 사용하는 필드만** 모델에 둔다. 필드 존재 여부는 "RFC 스펙 전체 표현"이 아닌 "앱 사용 여부"가 기준이다.

### RFC와 의미가 다른 필드는 도메인 명칭을 사용

RFC 5545의 표기를 그대로 쓰면 **의미가 달라 오해를 부르는 필드**는 `diary*` 접두 등 도메인 명칭으로 명명한다.

- `RoutineRRule.diaryByDay: RRuleDiaryByDay` — RFC 5545의 `BYDAY`와 표기는 닮았지만 의미가 다르다.
  - RFC `BYDAY` ordinal: "그 발생 주기 내 N번째 발생"
  - Diary `RRuleDiaryByDay.ordinal`: **일요일 시작 캘린더의 N번째 주** (`null`=매주, `>0`=N번째 주, `<0`=끝에서 N번째 주)
  - 자세한 의미는 `RRuleDiaryByDay`의 KDoc 참고.

### 미사용 규칙은 구현하지 않음

실제 앱에서 사용하지 않는 규칙은 **모델/계산/유효성 검증/UI** 어느 레이어에도 구현하지 않는다.

- 신규 규칙을 사용하게 되면 그 시점에 모든 레이어에 추가한다.
- 테스트 범위도 이 원칙을 따른다 — 구현된 규칙만 TC 작성 대상.

## 3. 핵심 개념

| 용어 | 구분 | 의미 |
|---|---|---|
| **RRULE** | RFC 5545 | 반복 규칙 (e.g. "매주 월/수/금"). `RoutineRRule` 데이터 클래스. `Routine.rRules`는 List이며 여러 규칙을 OR 결합한다. |
| **RDATE** | RFC 5545 | RRULE 외에 추가로 포함할 명시적 날짜 집합. `Routine.rDates: Set<LocalDate>`. |
| **EXDATE** | RFC 5545 | RRULE에서 제외할 명시적 날짜 집합. `Routine.exDates: Set<LocalDate>`. |
| **diaryByDay** | 앱 특화 | RFC `BYDAY`와 다른 도메인 표현. 요일 집합 + "월 내 N번째 주" ordinal. `RRuleDiaryByDay`. |
| **byMonthDay** | RFC 5545 | RFC `BYMONTHDAY`와 동일. `RoutineRRule.byMonthDay: Set<Int>`. |
| **routineCount** | 앱 특화 | **발생일 1회당 수행 횟수**. 예: "매일 물 8회 마시기" → `8`. RRULE이 발생일을, `routineCount`가 발생일 내 반복 횟수를 결정한다. `RoutineDetail.routineCount`. |
| **isCalendarVisible** | 앱 특화 | 캘린더 뷰에 노출할지 여부. `true`(기본값)면 `CalendarRoutine` 산출 대상이고, `false`면 캘린더 쿼리에서 제외된다. `Routine.isCalendarVisible`. |

RFC 5545 참고: <https://datatracker.ietf.org/doc/html/rfc5545>

## 4. 참고

- RFC 5545: <https://datatracker.ietf.org/doc/html/rfc5545>
- 이전 구현: `git show 9ffa1a4e` 및 그 이전 커밋들 (RoutineV2 이전)
- 관련 모듈
  - 모델: `core/model` (패키지 `...core.model.routine`)
  - 도메인/데이터: `domain/routine`, `data/routine`
  - UI: `feature/routine`
