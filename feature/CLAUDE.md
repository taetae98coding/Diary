# Feature

@.claude/docs/compose-conventions.md

화면 단위 모듈. 각 feature는 독립적으로 동작한다.

## 지침

### 모듈 구성

각 feature 모듈은 다음 요소로 구성된다:

- `Feature{Name}Module.kt` — Koin DI 모듈 (`@Module @ComponentScan @Configuration`)
- `{Name}EntryProvider.kt` — Navigation3 화면 등록 (`EntryProviderScope<NavKey>`)
- 하위 디렉토리 — home, add, detail 등 화면별 패키지

### Navigation 패턴

```kotlin
// EntryProvider에서 화면 등록
entry<MemoHomeNavKey> {
    val scope = rememberKoinScope<MemoHomeScope>(scopeId = MemoHomeScope.DEFAULT_ID, autoClose = true)
    MemoHomeScreen(
        navigateToX = { backStack.add(NavKeyX) },
        navigateUp = backStack::removeLastOrNull,
    )
}
```

- NavKey는 `:core:navigation` 모듈에 정의한다.
- Dialog는 `DialogSceneStrategy.dialog()` 메타데이터를 사용한다.

### ViewModel 패턴

- `@KoinViewModel`으로 Koin에 등록한다.
- UI 상태는 `StateFlow` + `SharingStarted.WhileSubscribed(5_000)`으로 노출한다.
- 화면에서 `collectAsStateWithLifecycle()`로 수집한다.

### Screen Composable 패턴

```kotlin
@Composable
internal fun XxxScreen(
    navigateToY: () -> Unit,       // 네비게이션 콜백
    modifier: Modifier = Modifier,
    viewModel: XxxViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scaffoldState = rememberXxxScaffoldState()

    XxxEffect(viewModel, scaffoldState)  // Side-effect 분리

    XxxScaffold(
        stateProvider = { state },       // Provider 람다로 lazy 평가
        ...
    )
}
```

- Screen은 `internal` 가시성.
- Side-effect는 별도 `@Composable` 함수로 분리한다.

### ScaffoldState

- Compose 전용 상태 (SnackbarHostState, DialogState 등)를 관리한다.
- `@Stable` 어노테이션을 붙인다.
- ViewModel 상태와 분리하여 관리한다.

### Scope 기반 DI (복잡한 화면)

- `@Qualifier` 어노테이션으로 Scope 정의
- Scope에 `DEFAULT_ID` 상수 포함
- `rememberKoinScope<ScopeClass>(scopeId = DEFAULT_ID, autoClose = true/false)`
