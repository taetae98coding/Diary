# Feature: Routine 모듈 PRD

> 관련 도메인 스펙: `.claude/docs/specs/routine.md`
> 상태: 초안 / 범위: `:feature:routine` 초기 구현 + `:domain:routine` 뼈대 생성

## 1. 개요

### 1.1 목적
`:feature:routine` 모듈을 신규 생성하여 Routine(반복 일정) 도메인의 **홈 리스트 화면**과 **추가 화면**을 제공한다. 이번 작업은 UI/네비게이션 계층을 완성하고, `:domain:routine`의 UseCase 시그니처까지 정의한다. `:data:routine` 구현 및 RRule 전개 로직은 후속 작업으로 분리한다.

### 1.2 범위 (In Scope)
- `:app` — `Routine` TopLevel NavEntry 등록, `Cmd+4` 단축키, BottomNavigation/Rail 아이콘·라벨
- `:feature:routine`
  - `RoutineHomeScreen` — 활성 Routine(`!isFinished && !isDeleted`) 리스트, `title` 오름차순 정렬
  - `RoutineAddScreen` — Title/Description/Color + 신규 `LocalDateRangeCard` + 신규 `RRuleCard`(다중 RRule)
  - ViewModel/StateHolder, NavKey, DI 모듈
- `:domain:routine`
  - `AddRoutineUseCase` 파일 생성 (생성자 시그니처·Clock 주입 포함)
  - `AccountRoutineRepository` 인터페이스 파일 생성 (메서드 시그니처만, body는 없음)
  - UseCase `invoke` 바디는 `TODO("feature/routine data 레이어 구현 후 연결")`
- 공통 컴포넌트 신규 구현
  - `LocalDateRangeCard` (start/endInclusive 각각 nullable)
  - `RRuleCard` / `RRulePicker` / `WeekdayNumChip` 등 RRule 편집용 Compose 집합
  - `RoutineIcon`

### 1.3 비범위 (Out of Scope)
- **`:data:routine` 구현** — Room Entity, DAO, DataSource, Repository 구현, 서버 동기화, 마이그레이션
- **RRule 전개 로직** — "오늘 발생 여부", 다중 RRule union dedup, rDates/exDates 반영 계산
- **"오늘 부합 루틴" 우선 정렬** — 스펙에서 제거. 단순 `title` 오름차순만
- **Routine 상세/편집/삭제 화면** — 이번 범위는 Home + Add만
- **`rDates` / `exDates` 편집 UI** — Add 화면에서 입력하지 않고 empty로 저장
- **"N회 후 종료" (RRule COUNT)** — 모델 미지원이라 UI에서도 미지원
- **`byMonthDay=31` / 윤년 UX 경고**

## 2. 사용자 시나리오

### 2.1 골든 패스
1. 사용자가 앱을 실행하고 하단 내비게이션에서 **루틴** 탭을 선택(또는 `Cmd+4`).
2. `RoutineHomeScreen`에서 활성 Routine 리스트를 본다. 각 카드는 **Color 점 + Title** 1줄.
3. 오른쪽 하단 `+` FAB를 누르면 `RoutineAddScreen`으로 이동.
4. 제목, 설명, 색상, (선택적) 시작일/종료일, **요일(byDay) 또는 일자(byMonthDay) 중 최소 1개**를 같은 화면에서 입력.
5. 저장 버튼을 누르면 Routine이 추가되고 홈으로 복귀 또는 Add 화면이 초기화된다 (MemoAdd 패턴과 동일).

### 2.2 엣지 케이스
| 케이스 | 동작 |
|---|---|
| 활성 Routine이 0개 | 빈 상태 플레이스홀더 ("루틴이 없어요" 등 — MemoHome/TagHome 패턴 재사용) |
| Title 공란으로 저장 시도 | 저장 버튼 비활성 + 포커스 Title (MemoAdd 패턴) |
| RRule 0개 상태로 저장 시도 | 저장 버튼 비활성. "규칙을 1개 이상 추가하세요" |
| 시작일만 / 종료일만 입력 | 각각 허용 (nullable 독립) |
| 시작일 > 종료일 | 저장 버튼 비활성 + Inline 에러 |
| Monthly `byMonthDay`와 `byDay` 동시 입력 | 모델은 허용. 사용자 결정 우선 |
| 네트워크 실패 (동기화) | 기존 MemoHome `SyncStatus.Failed` 패턴 — "오프라인입니다." Snackbar |
| 화면 회전 / 프로세스 사망 | `rememberSaveable` + Saver로 Title/Description/Color/DateRange 복원. RRule 리스트는 `.retain()` (List 복잡도 대응) |

## 3. 유사 서비스 조사

### 3.1 참고 서비스
| 서비스 | 채택 여부 | 차용 포인트 |
|---|---|---|
| **Google Calendar** | ⭐ 최우선 | Custom 반복 다이얼로그 구조, Monthly 날짜형/요일형 라디오 토글, 요일 7칸 세그먼트, 종료 조건 UI |
| Apple/iCal (RFC 5545) | 준수 | 필드명·의미는 표준을 따름. UI는 추상화 |
| Nylas 엔지니어링 회고 | 주의사항 참고 | `byMonthDay` 모호성, 다중 RRule dedup 이슈 — 대부분 data 레이어 몫이라 이번 범위는 aware만 |

### 3.2 채택 패턴
1. **`RoutineRRule` 필드 1:1 매핑** — UI 추상화 레이어(canonical 변환, Monthly 탭 상호배타 등) 없이 모델 필드를 그대로 편집. 사용자(개인)가 RRule 개념 이해한다는 전제. 구현·유지보수 복잡도 최소화.
2. **다중 RRule = AddScreen 인라인 카드 리스트**. 각 카드에 ByDayEditor + ByMonthDayEditor + 삭제 버튼. 하단 "규칙 추가" 버튼. 라벨 의미: OR 합집합. 빈 RRule(둘 다 미입력)은 저장 시 자동 제외
3. **종료 조건은 DateRange로 통합** — 별도 "없음 / 날짜 / N회" 라디오는 제공하지 않음. 종료일 없음 = `endInclusive = null`.

### 3.3 배제 / 보류
- "N회 후 종료" — 모델 미지원
- **Monthly 날짜형/요일형 탭 상호배타** — 모델은 동시 설정 허용하므로 UI에서도 차단하지 않음. 사용자가 원하는 대로 입력
- **Google Calendar 프리셋 / 자연어 파서** — 이번 범위 밖. 필드 직접 편집만 제공

## 4. 도메인 모델

### 4.1 기존 모델 (재사용)
`core/model/routine/` 에 RoutineV2로 이미 정의됨. 변경 없음.

- `Routine` (`Routine.kt`) — `id`, `detail`, `rRules: List<RoutineRRule>`, `rDates`, `exDates`, `isFinished`, `isDeleted`, `updatedAt`, `createdAt`
- `RoutineDetail` (`RoutineDetail.kt`) — `title`, `description`, `start: LocalDate?`, `endInclusive: LocalDate?`, `color: Int`, `routineCount: Int`
- `RoutineRRule` (`RoutineRRule.kt`) — `byDay: RRuleByDay`, `byMonthDay: Set<Int>`. 이번 배포에서는 이 두 필드만. `frequency`/`interval`/`weekStart`/`bySetPos`는 후속 배포 범위. RFC 5545 호환은 데이터 레이어 직렬화 시점에 기본 `FREQ=MONTHLY`로 채움
- `RRuleByDay` (`RRuleByDay.kt`) — `days: Set<DayOfWeek>`, `ordinal: Int?`. 모든 요일이 동일 ordinal 공유 (uniform). RFC 5545 BYDAY의 mixed-ordinal(`BYDAY=1MO,2TU` 등)은 `Routine.rRules` 다중 항목으로 분해 표현

### 4.2 신규 엔티티
없음. Add 화면은 기존 모델만 생성한다.

### 4.3 DB / 마이그레이션
**이번 범위에서 건드리지 않는다.** `:data:routine` 작업 시 후속 설계. `AddRoutineUseCase` 바디가 TODO이므로 DB 미변경.

## 5. 레이어 설계

레이어 방향: `domain` ← `data` ← `feature` ← `app` (ProjectGuard 유지)

### 5.1 `:domain:routine` (신규 모듈)
경로: `domain/routine/`
`settings.gradle.kts` 등록 필요.

| 파일 | 내용 |
|---|---|
| `AccountRoutineRepository.kt` | 인터페이스. 메서드: `suspend fun add(routine: Routine)`. (후속 작업에서 `find`/`update`/`delete`·Flow 등 추가) |
| `usecase/AddRoutineUseCase.kt` | `class AddRoutineUseCase()` — 생성자 의존성 없음 (data 레이어 미구현 시 Koin이 eager 해결을 시도하므로). `suspend operator fun invoke(detail: RoutineDetail, rRules: List<RoutineRRule>): Result<Unit>` — body는 `TODO("...")`. **후속 작업**: `:data:routine` 구현 시 `AccountRoutineRepository`, `GetAccountUseCase`, `RequestSyncUseCase` 주입 복원 (Memo/Tag 패턴 기준. Clock은 Repository impl에 주입). |

`build.gradle.kts` — `feature/memo`의 domain 모듈 템플릿을 따른다. `projects.core.model`, `projects.domain.account`, `projects.domain.sync` 등 의존.

### 5.2 `:data:routine`
**이번 범위 밖.** 빈 모듈을 만들지 않는다. `AccountRoutineRepository` 구현은 후속 PR에서 추가.

### 5.3 `:feature:routine` (신규 모듈)
경로: `feature/routine/`. `feature/memo/build.gradle.kts` 템플릿을 따른다.

#### 5.3.1 NavKey (core/navigation)
- `core/navigation/.../RoutineHomeNavKey.kt` — `@Serializable data object RoutineHomeNavKey : NavKey`
- `core/navigation/.../RoutineAddNavKey.kt` — `@Serializable data object RoutineAddNavKey : NavKey`

#### 5.3.2 Home
```
feature/routine/src/commonMain/kotlin/.../home/
├── RoutineHomeScreen.kt         // internal, NavGraphBuilder 확장 routineHomeEntry()
├── RoutineHomeScaffold.kt        // Scaffold 분리
├── RoutineHomeViewModel.kt       // Koin ViewModel, routineFlow + syncStatus
├── RoutineHomeUiState.kt         // @Stable
└── component/RoutineCard.kt      // 1줄: ColorCircle + Title (TagListScaffold.kt:139-171 패턴)
```

#### 5.3.3 Add
```
feature/routine/src/commonMain/kotlin/.../add/
├── RoutineAddScreen.kt           // internal
├── RoutineAddScaffold.kt
├── RoutineAddStateHolder.kt      // @Scope @Scoped, 다중 RRule 리스트 관리
├── RoutineAddScaffoldState.kt    // Card 상태 집약, detail/rRules computed
└── component/
    ├── ByDayEditor.kt             // RRuleByDay — 요일 다중 토글 + 단일 ordinal 칩
    ├── ByMonthDayEditor.kt        // Set<Int> — 1~31 그리드 + "-1(마지막 날)" 토글
    ├── RRuleEntryState.kt         // 단일 RRule 묶음 (byDay + byMonthDay)
    └── RRuleEntrySection.kt       // 한 카드 = "반복 규칙 N" 헤더 + 두 에디터 + 삭제 버튼
```

#### 5.3.4 DI 모듈
`FeatureRoutineModule.kt` — `FeatureMemoModule.kt` 패턴 (`@Module @ComponentScan @Configuration`).

### 5.4 `:app` 변경

| 파일 | 변경 내용 |
|---|---|
| `app/shared/.../TopLevelDestination.kt` (9~14) | `enum`에 `Routine` 추가. 순서: `Memo, Tag, Calendar, Routine, More` |
| `app/shared/.../AppScaffold.kt` (39~61) | `onPreviewKeyEvent`의 `Cmd+N` 맵 재정의. **`Cmd+4 → Routine`, `Cmd+5 → More`**. `Cmd+1~3`은 유지 |
| `app/shared/.../AppScaffold.kt` (70~83) | `when(destination)` 아이콘/라벨 분기에 `Routine -> RoutineIcon() / "루틴"` 추가 |
| `app/shared/.../AppNavigation.kt` (39~46) | `EntryProvider`에 `routineHomeEntry(backStack)`, `routineAddEntry(backStack)` 추가 |
| `app/shared/build.gradle.kts` (43~47) | `implementation(projects.feature.routine)` 추가 |
| `settings.gradle.kts` | `:domain:routine`, `:feature:routine` include 추가 |

## 6. UI 컴포넌트 재사용 vs 신규

### 6.1 재사용 (그대로)
| 컴포넌트 | 경로:라인 |
|---|---|
| `TitleCard` | `compose/core/.../card/TitleCard.kt:15` |
| `DescriptionCard` | `compose/core/.../card/DescriptionCard.kt:31` |
| `ColorCard` + `ColorPickerHost` | `compose/core/.../card/ColorCard.kt:25` |
| `LocalDatePickerDialogHost` | `compose/core/.../dialog/` |
| `AddFloatingButton` | `compose/core/.../button/AddFloatingButton.kt` |
| `NavigateUpButton` | `compose/core/.../button/NavigateUpButton.kt` |
| `ColorCircle` | `feature/tag/.../home/TagListScaffold.kt:154` 기준 import |
| `focusableKeyEvent` | `compose/core/.../modifier/` |

### 6.2 신규 컴포넌트 (Compose 컨벤션 준수)
각 컴포넌트는 다음을 갖춘다: `@Composable public fun`, `@Stable` State 클래스, `rememberXxxState()` (Saver 또는 `.retain()`), `XxxDefaults` (colors/shapes), `@ComponentPreview` + `DiaryTheme` 래핑.

| 컴포넌트 | 위치 | 역할 | 근거 |
|---|---|---|---|
| `LocalDateRangeCard` | `compose/core/.../card/LocalDateRangeCard.kt` | `start: LocalDate?`, `endInclusive: LocalDate?` 각각 독립 nullable. 각 필드 옆에 "지우기" 액션 | 기존 `DateRangeCard`는 `LocalDateTime + isAllDay + hasDateRange` 전체 토글이라 의미 불일치. 다른 feature에서도 재사용 가능하므로 core에 배치 |
| `RoutineCard` | `feature/routine/.../home/component/RoutineCard.kt` | 1줄: `ColorCircle + Title`. Material3 `Card` | TagListScaffold.kt:139-171 참고. 단일 feature 전용 |
| `RoutineIcon` | `compose/core/.../icon/RoutineIcon.kt` | Material Icons 기반 (예: `Repeat`). `@IconPreview` | TagIcon 패턴 |
| `ByDayEditor` + `ByMonthDayEditor` + `RRuleEntrySection` | `feature/routine/.../add/component/` | §5.3.3 참조 | AddScreen 인라인 다중 카드 편집. 단일 feature 전용 |

### 6.3 RRule 편집 UX 상세 (필드 1:1 매핑)

`byDay`와 `byMonthDay` 두 필드를 `RoutineAddScreen`에서 카드 리스트로 인라인 노출한다. 카드는 N개까지 추가 가능. 한 Routine = 여러 RRule(OR 합집합).

| 필드 | 컴포넌트 | UI 상세 |
|---|---|---|
| `byDay` | `ByDayEditor` | 두 줄 구성. **위 줄**: 요일 7칸 토글 (다중 선택). **아래 줄**: `매주`(ordinal=null) / `1번째` ~ `5번째` 단일 선택. 모든 선택 요일은 동일 ordinal을 공유 (`-1`은 미지원). |
| `byMonthDay` | `ByMonthDayEditor` | 1~31 숫자 그리드(7열). 별도 `마지막 날` 토글 (-1). |

RRule 요약 포매팅은 후속 배포 범위 (현재 RRule 카드 노출 화면 없음).

## 7. 플랫폼 제약

- **타겟**: Android / iOS / JVM / WASM (feature/memo와 동일).
- **`expect`/`actual` 필요 없음** — common만 사용. 단축키(`Cmd+N`)는 `AppScaffold` 레벨에서 이미 처리 중.
- **WASM**: `LocalDatePickerDialog`가 모든 타겟에서 동작하는지 기존 `DateRangeCard` 재사용 상태 확인 — 확인 필요 (재사용 컴포넌트라 가정)

## 8. 동기화 / 오프라인 전략

- **이번 범위는 `AddRoutineUseCase` 바디가 TODO이므로 실제 동기화 미수행.** UI 플로우와 에러 핸들링 뼈대만 맞춘다.
- `RoutineHomeScreen` — MemoHome의 `SyncStatus.Failed` 패턴 복제: Snackbar "오프라인입니다.". `syncStatusUseCase` Flow를 ViewModel에서 collect.
- `RoutineAddScreen` — 저장 후 `runCatching` 실패 시 Snackbar (MemoAdd 패턴). 후속 작업에서 `AddRoutineUseCase` 내부가 구현되면 실제 실패가 전달됨.
- 충돌 해결은 `:data:routine` 후속 PR 범위.

## 9. 엣지 케이스 / 에러 시나리오

| 시나리오 | UX |
|---|---|
| Title blank | 저장 버튼 비활성. 힌트 "제목을 입력하세요" |
| 모든 RRule 카드가 비어있음 | 저장 버튼 비활성. 적어도 한 카드는 byDay 또는 byMonthDay 입력 필요. 빈 카드는 저장 시 자동 제외 |
| RRule 카드 1개 (마지막) | 삭제 버튼 미노출 (최소 1개 카드 유지) |
| `start > endInclusive` | 저장 버튼 비활성. 날짜 필드 아래 inline 에러 |
| `byDay`/`byMonthDay` 모두 empty | RRule 저장 허용. 데이터 레이어에서 기본 FREQ 채워짐 |
| 빈 Routine 리스트 | 플레이스홀더 + FAB 노출 |
| 동기화 실패 | Home·Add 모두 "오프라인입니다." Snackbar |
| 화면 회전 | §2.2 복원 규칙 |
| 단축키 충돌 | `AppScaffold`의 `Cmd+N` 스위치가 이미 글로벌 처리. Add 화면 내에서는 `Cmd+Enter`(저장) 기존 패턴 유지 |

### 결정 필요 / 확인 필요 항목
- `RRuleSummaryFormatter`의 포맷 문자열 집합 (§10.3 표 기반으로 구현 시 확장)

## 10. TC 설계

### 10.1 UseCase TC — `:domain:routine/src/jvmTest`
**이번 범위는 Feature 구현이므로 생략한다.** `AddRoutineUseCase.invoke`가 `TODO()` 바디이므로 테스트가 의미 없음. Koin smoke 테스트도 포함하지 않는다. 후속 PR에서 data 레이어 연결 시 일괄 작성.

### 10.2 Compose TC — `:feature:routine/src/androidDeviceTest`
환경: `allSdksGroupAndroidDeviceTest` (Compose 모듈 룰).

| 대상 | 시나리오 |
|---|---|
| `RoutineCard` | Title/Color 렌더 확인. 클릭 이벤트 콜백 호출 |
| `RoutineHomeScaffold` | 빈 상태 렌더, 리스트 렌더, FAB 클릭 콜백 |
| `LocalDateRangeCard` | start/end 각각 nullable 상태 렌더. 지우기 액션으로 null 전환. 자동 보정 |
| `ByDayEditor` | 요일 토글 / ordinal 단일 선택(매주·1~5번째) |
| `ByMonthDayEditor` | 1~31 그리드 + `마지막 날` 토글 |
| `RoutineAddScaffold` | Title blank / RRule 0개 / 날짜 역전 시 저장 비활성. 정상 입력 시 onSave 콜백 확인 |

### 10.3 Mapper TC
**이번 범위 없음.** mapper는 data 레이어에서 등장. 단, `RRuleSummaryFormatter`는 순수 함수이므로 `:feature:routine/src/jvmTest`에서 Kotest `BehaviorSpec`으로 커버:

| Given | When | Then |
|---|---|---|
| `RoutineRRule(byDay=RRuleByDay({MON,WED}))` | format() | "월,수" |
| `RoutineRRule(byMonthDay={15})` | format() | "15일" |
| `RoutineRRule(byMonthDay={-1})` | format() | "마지막 날" |
| `RoutineRRule(byDay=RRuleByDay({TUE}, 3))` | format() | "3번째 화" |
| `RoutineRRule(byDay=RRuleByDay({MON}), byMonthDay={15})` | format() | "월 · 15일" |
| `RoutineRRule()` | format() | "" |

### 10.4 Clock / Side-input 주입
- `AddRoutineUseCase`는 `Clock` 생성자 주입 — body TODO 단계에서도 시그니처에 포함
- Compose 내 `today` 필요 시 State 클래스에서 `Clock.System.todayIn(...)` 허용 (기존 `DateRangeCardState`와 동일 — Compose 컨벤션 예외)

## 11. 구현 Phase

각 Phase는 **독립적으로 빌드·테스트·머지 가능**하다. 머지 후 앱은 항상 green(컴파일·ProjectGuard·기존 테스트 통과, 실기기 구동)을 유지한다.

### Phase 의존 그래프

```
Phase 1 (모듈 뼈대)
   ├── Phase 2 (NavEntry + 빈 Home)
   ├── Phase 4 (RRule 편집 컴포넌트)
   └── ─────────────┐
                    ↓
Phase 3 (LocalDateRangeCard) — 독립
                    ↓
             Phase 5 (Add 화면 조립)
```

Phase 2, 3, 4는 Phase 1 완료 후 **병렬 진행 가능**. Phase 3은 `compose/core`만 건드리므로 Phase 1과도 독립.

---

### Phase 1 — 모듈 뼈대 + Domain 레이어

**목표**: `:domain:routine`, `:feature:routine` 빈 모듈 생성. UseCase 시그니처까지만 정의, body TODO.

**산출물**
- `settings.gradle.kts` — `:domain:routine`, `:feature:routine` include
- `domain/routine/build.gradle.kts` — `feature/memo/domain` 템플릿
- `domain/routine/src/commonMain/kotlin/.../AccountRoutineRepository.kt` — `suspend fun add(routine: Routine)`
- `domain/routine/src/commonMain/kotlin/.../usecase/AddRoutineUseCase.kt` — 생성자 주입 (`repository`, `clock`, `getAccountUseCase`, `requestSyncUseCase`), `invoke(detail, rRules): Result<Unit>` body = `TODO(...)`
- `feature/routine/build.gradle.kts` — `feature/memo/build.gradle.kts` 템플릿
- `feature/routine/src/commonMain/kotlin/.../FeatureRoutineModule.kt` — `@Module @ComponentScan @Configuration` 뼈대
- `core/navigation/.../RoutineHomeNavKey.kt`, `RoutineAddNavKey.kt`

**검증**
```bash
./gradlew spotlessApply
./gradlew projectGuardCheck
./gradlew :domain:routine:jvmTest :feature:routine:compileKotlinJvm
```

**의존**: 없음 (첫 Phase)

---

### Phase 2 — `:app` NavEntry + 빈 RoutineHomeScreen

**목표**: 앱에서 Routine 탭으로 이동 가능. 빈 상태 화면 + FAB 동작. FAB는 Placeholder `RoutineAddScreen`으로 이동.

**산출물**
- `compose/core/.../icon/RoutineIcon.kt`
- `feature/routine/.../home/RoutineHomeScreen.kt` (internal + `routineHomeEntry(backStack)`)
- `feature/routine/.../home/RoutineHomeScaffold.kt`
- `feature/routine/.../home/RoutineHomeViewModel.kt` — Repository Flow 구독(현재 Repository에 Flow 메서드 없음 → `emptyFlow()` stub 또는 Phase 1에서 인터페이스에 stub Flow 추가 필요)
- `feature/routine/.../home/RoutineHomeUiState.kt`
- `feature/routine/.../home/component/RoutineCard.kt` (ColorCircle + Title 1줄)
- `feature/routine/.../add/RoutineAddScreen.kt` — **Placeholder** (TopAppBar + "준비 중" 문구). Phase 5에서 대체
- `feature/routine/.../add/routineAddEntry.kt` — NavEntry 등록
- `app/shared/.../TopLevelDestination.kt` — `Routine` enum 추가 (순서: `Memo, Tag, Calendar, Routine, More`)
- `app/shared/.../AppScaffold.kt` — 단축키 `Cmd+4 Routine, Cmd+5 More`, 아이콘/라벨 분기 추가
- `app/shared/.../AppNavigation.kt` — `routineHomeEntry`, `routineAddEntry` 등록
- `app/shared/build.gradle.kts` — `projects.feature.routine` 의존 추가

**검증**
```bash
./gradlew spotlessApply
./gradlew projectGuardCheck
./gradlew :feature:routine:allSdksGroupAndroidDeviceTest \
  -Pandroid.testInstrumentationRunnerArguments.class=...RoutineHomeScaffoldTest
./gradlew :app:android:assembleDebug
```
실기기 스모크: BottomNav Routine 선택 / `Cmd+4` / 빈 상태 렌더 / FAB 클릭 시 Placeholder 이동 / 뒤로가기 / `Cmd+5` More 이동

**의존**: Phase 1

---

### Phase 3 — `LocalDateRangeCard` (공통 컴포넌트)

**목표**: `compose/core`에 `LocalDate` nullable 각각 지원하는 DateRange Card 추가. Phase 5 조립 이전에 완성.

**산출물**
- `compose/core/.../card/LocalDateRangeCard.kt` — Main Composable
- `compose/core/.../card/LocalDateRangeCardState.kt` — `@Stable`, `start/endInclusive` MutableState<LocalDate?>
- `LocalDateRangeCardDefaults` — colors/shapes
- `rememberLocalDateRangeCardState` + `Saver`
- `@ComponentPreview` + `DiaryTheme`
- Compose TC — start/end 각각 null 상태 / 지우기 액션 / start > end 에러 표시

**검증**
```bash
./gradlew spotlessApply
./gradlew :compose:core:allSdksGroupAndroidDeviceTest \
  -Pandroid.testInstrumentationRunnerArguments.class=...LocalDateRangeCardTest
```

**의존**: 없음 (Phase 1과 독립, 병렬 가능)

---

### Phase 4 — RRule 편집 컴포넌트 세트 + Formatter

**목표**: `RoutineRRule` 두 필드(byDay, byMonthDay)를 편집할 수 있는 Compose 컴포넌트 집합 완성. Phase 5 조립 이전에 완성.

**산출물**
`feature/routine/src/commonMain/kotlin/.../add/component/`:
- `ByDayEditor.kt` — RRuleByDay 편집 (요일 다중 토글 + 단일 ordinal)
- `ByMonthDayEditor.kt` — 1~31 그리드 + `마지막 날` 토글
- `RRuleSummaryFormatter.kt` — 순수 함수

**검증**
```bash
./gradlew spotlessApply
./gradlew :feature:routine:jvmTest --tests "*RRuleSummaryFormatterSpec"
./gradlew :feature:routine:allSdksGroupAndroidDeviceTest \
  -Pandroid.testInstrumentationRunnerArguments.class=...RRule*Test
```

**의존**: Phase 1 (`:feature:routine` 모듈 존재 필요)

---

### Phase 5 — `RoutineAddScreen` 조립 + UseCase 연결

**목표**: Phase 2 Placeholder Add 화면을 실제 입력 폼으로 대체. AddRoutineUseCase 호출 연결 (body는 여전히 TODO).

**산출물**
- `feature/routine/.../add/RoutineAddScreen.kt` — Placeholder 대체
- `feature/routine/.../add/RoutineAddScaffold.kt`
- `feature/routine/.../add/RoutineAddViewModel.kt` — `@KoinViewModel`. `add(detail, rRules)` 호출. isInProgress/effect StateFlow
- `feature/routine/.../add/RoutineAddScaffoldState.kt` — Card 상태 집약 + `rRuleEntries: SnapshotStateList<RRuleEntryState>`, `addRule()`/`removeRuleAt()`/`resetRules()`. `detail`/`rRules`/`canSubmit` computed
- Title/Description/Color 재사용 연결
- Phase 3 `LocalDateRangeCard` 연결
- Phase 4 `ByDayEditor` / `ByMonthDayEditor` / `RRuleEntrySection` 인라인 연결
- AddRoutineUseCase 호출 + `runCatching`. 빈 RRule은 자동 제외
- Sync 실패 Snackbar (MemoAdd 패턴)
- Compose TC — Title blank / 모든 RRule empty 시 저장 비활성, 정상 입력 시 onSave 콜백

**검증**
```bash
./gradlew spotlessApply
./gradlew projectGuardCheck
./gradlew :feature:routine:allSdksGroupAndroidDeviceTest \
  -Pandroid.testInstrumentationRunnerArguments.class=...RoutineAddScaffoldTest
./gradlew :app:android:assembleDebug
```
실기기 스모크: Home FAB → Add → Title/Description/Color/DateRange/RRule 입력 → 저장(body TODO이므로 에러 Snackbar 노출 확인)

**의존**: Phase 1, 2, 3, 4

---

## 12. 작업 완료 체크리스트 (전체 머지 후)

1. `./gradlew spotlessApply`
2. `./gradlew projectGuardCheck`
3. `./gradlew :feature:routine:allSdksGroupAndroidDeviceTest`
4. `./gradlew :feature:routine:jvmTest`
5. `./gradlew :app:android:assembleDebug`
6. 실기기 스모크: §11 Phase 5 실기기 스모크 항목

## 13. 후속 작업 (참고)

- `:data:routine` 구현 — Room Entity(+자식 RRule/RDate/EXDate 테이블), DAO, 마이그레이션, Repository 구현, 서버 동기화
- RRule 전개 엔진 — `dmfs/lib-recur` KMP 래핑 또는 자체 구현. "오늘 발생 여부" 계산
- Routine 상세/편집/삭제 화면
- "오늘 부합 루틴" 우선 정렬 재도입 검토