# Client Domain

## 지침

- UseCase는 행위 위주로 설계한다.
- Repository 함수는 CRUD 위주로 설계한다.
- Flow를 반환하는 UseCase는 `flow { emitAll(repository.get()) }` 패턴으로 감싸서 repository 함수 호출 시점의 예외도 `catch`에서 `Result.failure`로 래핑되도록 한다.
