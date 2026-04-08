# Data

Repository 구현체와 DataSource 조합 레이어.

## 지침

### Repository 구현 패턴

- Result/Either를 사용하지 않는다. 에러는 자연스럽게 전파한다.

### DataSource 의존

- core 모듈이 제공하는 DataSource **인터페이스**에 의존한다 (구현체 직접 참조 금지).
- 네이밍: `{Entity}{Location}DataSource` (Location: Local, Remote, DataStore)

### 다중 DataSource 조합

- 트랜잭션이 필요하면 `DatabaseTransactor`를 사용한다.
- 타임스탬프: `clock.now().toEpochMilliseconds()` (Clock은 생성자로 주입받아 사용, `Clock.System` 직접 사용 금지)
- 매핑: `toDomain()`, `toLocal()` 등 mapper 확장 함수 사용

### Koin 등록

- 대부분 `@Factory`. 내부 상태를 유지하는 경우만 `@Single`.
