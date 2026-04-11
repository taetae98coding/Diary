# Compose

@.claude/docs/compose-conventions.md

Compose Multiplatform UI 컴포넌트 모듈.

- `compose/core` — 공통 컴포넌트, 테마, 유틸리티
- `compose/calendar` — 캘린더 전용 컴포넌트

## 지침

### 컴포넌트 구조

하나의 컴포넌트는 다음 요소로 구성된다:

1. **Main Composable** — `@Composable public fun ComponentName(...)`
2. **State 클래스** — `@Stable` 어노테이션, 내부 상태 관리
3. **remember 함수** — `rememberComponentState()`, `rememberSaveable` + 커스텀 `Saver` 또는 `.retain()` 사용
4. **Defaults 오브젝트** — `ComponentDefaults`에서 `colors()`, `shapes()` 팩토리 제공
5. **Preview** — `@ComponentPreview` / `@IconPreview` / `@ScreenPreview` + `DiaryTheme` 래핑

### Icon 패턴

```kotlin
@Composable
public fun NameIcon(modifier: Modifier = Modifier) { ... }
```

- Material Icon 래핑
- `@IconPreview` + `DiaryTheme` 사용
