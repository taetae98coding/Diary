---
name: diary-review
description: |-
  프로젝트 내 기존 코드의 품질/구조/성능에 대한 평가성 질문 시 실행한다.
  TRIGGER when: "리뷰해줘", "검토해줘", "봐줘", "어때?", "괜찮아?", "성능 이슈 있어?", "개선할 점 있어?" 류 요청; 특정 파일/클래스/Composable에 대한 품질 평가.
  SKIP: PR 전체 리뷰(글로벌 `/review` 사용); 코드 작성/수정 요청; 버그 수정 요청; "어떻게 구현해?" 같은 구현 방법 질문.
argument-hint: [대상 파일 또는 컴포넌트 이름]
allowed-tools: Read, Grep, Glob, Agent
---

코드를 리뷰한다.

대상: `$ARGUMENTS`

## 리뷰 항목

### 아키텍처 & 설계

- 레이어 의존 방향(`domain ← data ← feature ← app`)을 지키는지 확인한다.
- SOLID 원칙, 특히 SRP(단일 책임)와 DIP(추상에 의존)를 지키는지 확인한다.
- 하나의 클래스/함수가 한 가지 일만 하는지 확인한다.
- Android Developer 문서 권장사항을 지키는지 확인한다.
- 참고 프로젝트(nowinandroid, Droidkaigi)와 유사한 구조인지 확인한다.

### Kotlin 관용구 (Idiomatic Kotlin)

- 불변성을 우선하는지 확인한다 (`val`, `data class`, `List` vs `MutableList`).
- null 안전성을 확보하는지 확인한다 (`!!` 남용, 불필요한 nullable 지양).
- 상태 모델링에 `sealed class` / `sealed interface`를 활용하는지 확인한다.
- scope function(`let`, `apply`, `run`, `with`, `also`)이 적절히 사용되는지 확인한다 (남용 경계).
- 확장 함수로 가독성을 확보하는지 확인한다.

### Coroutines & Flow

- Structured concurrency를 따르는지 확인한다 (lifecycle-aware scope 사용).
- Dispatchers 선택이 적절한지 확인한다 (`IO`, `Default`, `Main` 용도 구분).
- Cold/Hot Flow 선택 근거가 명확한지 확인한다 (`StateFlow` vs `SharedFlow` vs `Flow`).
- Cancellation에 안전한지 확인한다 (suspending point, `ensureActive`).
- 예외 처리가 명시적인지 확인한다 (`runCatching`, `supervisorScope`, `CoroutineExceptionHandler`).

### Compose 최적화

- State hoisting이 적절한지 확인한다 (stateless Composable 분리).
- 안정성을 고려했는지 확인한다 (`@Stable`, `@Immutable`, immutable collection).
- Recomposition 범위가 최소화되는지 확인한다 (람다 파라미터, 상태 분리).
- Side effect API를 올바르게 사용하는지 확인한다 (`LaunchedEffect`, `rememberUpdatedState`, `DisposableEffect`, `produceState`).
- 비용이 큰 연산에 `remember` / `derivedStateOf`가 적용되는지 확인한다.
- `LazyColumn` 등에 `key`가 지정되어 있는지 확인한다.

### 테스트 가능성

- 의존성이 생성자 주입으로 테스트 가능한지 확인한다.
- 순수 함수를 선호하는지 확인한다.
- `Clock`, 랜덤, ID 생성기 등 side-input이 주입되는지 확인한다 (`Clock.System` 직접 사용 금지).

### 네이밍 & 가독성

- 의도를 드러내는 이름(intention-revealing names)을 사용하는지 확인한다.
- UseCase는 행위 중심, Repository는 CRUD 중심 네이밍을 따르는지 확인한다.
- Command-Query Separation이 지켜지는지 확인한다.
- 코드로 충분히 표현 가능한 내용을 주석으로 쓰지 않는지 확인한다.

### KMP 특유

- `expect`/`actual`이 꼭 필요한 경우에만 사용되는지 확인한다.
- `commonMain`에 최대한 코드를 두고 플랫폼별 코드를 최소화했는지 확인한다.

### 성능

- 불필요한 recomposition / 메모리 누수가 없는지 확인한다.
- Flow/Coroutine이 lifecycle 밖으로 유출되지 않는지 확인한다.

## 논이슈 (리뷰 대상에서 제외)

- 실험적 API 사용
- 한국어 하드코딩
