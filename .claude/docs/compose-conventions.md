## Compose 컨벤션

### 컴포넌트 구조

하나의 컴포넌트는 다음 요소로 구성된다:

1. **Main Composable** — `@Composable public fun ComponentName(...)`
2. **State 클래스** — `@Stable` 어노테이션, 내부 상태 관리
3. **remember 함수** — `rememberComponentState()`, `rememberSaveable` + 커스텀 `Saver` 또는 `.retain()` 사용
4. **Defaults 오브젝트** — `ComponentDefaults`에서 `colors()`, `shapes()` 팩토리 제공
5. **Preview** — `@ComponentPreview` / `@IconPreview` / `@ScreenPreview` + `DiaryTheme` 래핑

### 파라미터 순서

1. 필수 파라미터 (state, data 등)
2. `modifier: Modifier = Modifier`
3. 선택적 상태/콜백
4. 테마 (colors, typography)
5. content 람다

### Provider 람다

상태를 전달할 때 `stateProvider: () -> T` 패턴으로 lazy 평가하여 recomposition을 최적화한다.

### Stateless Composable

- 파라미터에 최대한 default value를 지정하여 호출부의 부담을 줄인다.

### 테마

- `DiaryTheme.colorScheme`, `DiaryTheme.typography`, `DiaryTheme.dimen` 사용

### Dialog 패턴

- `DialogState` (isVisible, show, hide) 로 상태 관리
- 별도 Host 컴포넌트로 분리: `ColorPickerHost`, `LocalDatePickerDialogHost`

### Icon 패턴

```kotlin
@Composable
public fun NameIcon(modifier: Modifier = Modifier) { ... }
```

- Material Icon 래핑
- `@IconPreview` + `DiaryTheme` 사용
