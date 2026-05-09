## Clock 주입 컨벤션

시간 관련 값(`now`, 타임스탬프, epoch millis 등)을 사용하는 모든 클래스는 `Clock`을 **생성자로 주입**받아 사용한다.

### 규칙

- `Clock.System`을 **직접 호출하지 않는다**.
- UseCase, Repository, DataSource 등 시간을 다루는 모든 레이어에 적용된다.
- 타임스탬프가 필요한 경우: `clock.now().toEpochMilliseconds()`.

### 예외: Compose 레이어

- Composable, `remember*` 함수, ScaffoldState 등 **Compose 레이어**에서는 `Clock.System` 직접 사용을 허용한다.
- 이유: Composable/`remember*` 시그니처에 `Clock`을 강제 주입하면 호출부 부담이 커지고, UI 상태 초기값에 사용되는 현재 시각은 테스트 결정성보다 호출 시점의 자연스러운 사용성이 더 중요하다.
- 대신 Compose 레이어가 호출하는 ViewModel/UseCase/Repository는 컨벤션을 지킨다.

### 이유

- 테스트에서 시간을 고정할 수 있어야 TC가 결정론적으로 동작한다.
- `Clock.System` 직접 사용은 테스트 시점에 mocking이 어렵고, 시스템 시간 의존성을 숨긴다.

### 테스트에서

- Kotest TC 작성 시 `Clock`은 mockk로 주입하거나 고정 시각을 반환하는 fake `Clock`을 사용한다.
