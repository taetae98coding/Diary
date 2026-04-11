# Domain

## 지침

### UseCase / Repository 네이밍

- UseCase는 **행위 중심**: Select, Unselect, Page, Finish, Restore
- Repository는 **CRUD 중심**: get, upsert, delete, update (add/remove 대신 upsert/delete)

### 에러 처리

- Repository는 Result로 감싸지 않고 순수 타입(Unit, Flow) 반환
- Result 래핑은 UseCase에서 `runCatching`으로 처리
- UseCase에서 `.also { }` 대신 `runCatching` 내부에서 순차 호출
- Flow 반환 UseCase는 `flow { emitAll(repository.get()) }` 패턴으로 감싸서 호출 시점 예외도 catch 가능하게 처리

### Clock

- `Clock.System`을 직접 사용하지 않고, 생성자로 주입받아 사용한다.

### 테스트 커버리지

- 단순 repository 호출로 끝나지 않는 UseCase는 항상 TC를 작성한다.