---
name: test
description: 대상 컴포넌트/클래스의 스펙을 파악하고 TC를 리스트업한 후 테스트 코드를 작성한다.
argument-hint: [대상 클래스 또는 컴포넌트 이름]
allowed-tools: Read, Grep, Glob, Edit, Write, Bash, Agent
---

# TC 작성 워크플로우

대상: `$ARGUMENTS`

## 절차

### 1단계: 스펙 파악

- 대상 클래스/컴포넌트의 소스 코드를 읽는다.
- 의존하는 타입과 내부 동작을 파악한다.
- 이미 존재하는 테스트 파일이 있으면 읽는다.

### 2단계: TC 리스트업

- 파악한 스펙을 기반으로 TC 목록을 표로 정리하여 사용자에게 보여준다.
- 사용자 확인을 받은 후 3단계로 진행한다.

### 3단계: TC 작성

아래 규칙을 반드시 준수한다.

### 4단계: TC 실행 및 검증

- 작성한 테스트를 실행하여 모두 성공하는지 확인한다.
- Android 디바이스 테스트 실행 명령: `./gradlew :<module-path>:allSdksGroupAndroidDeviceTest -Pandroid.testInstrumentationRunnerArguments.class=<fully-qualified-class-name>`
- Kotest 테스트 실행 명령: `./gradlew :<module-path>:jvmTest --tests "<fully-qualified-class-name>"`
- 실패하는 테스트가 있으면 원인을 분석하고 수정한 후 재실행한다.
- 모든 테스트가 성공할 때까지 반복한다.

#### 환경 결정

- Compose 의존성이 있는 모듈 → `androidDeviceTest`에 JUnit4로 작성
- Compose 의존성이 없는 모듈 → `jvmTest`에 Kotest로 작성

#### Kotest (UseCase 등)

- `BehaviorSpec`을 사용한다.
- Given/When/Then 패턴으로 작성한다.
- UseCase 내부에 정의된 에러만 TC로 작성한다. Repository나 의존성이 걸려있는 다른 UseCase에서 발생하는 에러는 TC에서 제외한다.
- UseCase의 흐름이 올바르게 동작하는지 검증한다.
  - Repository 또는 의존성이 있는 UseCase를 호출하는지 검증한다.
  - 호출 순서가 올바른지 검증한다.

#### JUnit4 (Compose)

- `createComposeRule()`을 사용한다. (import 경로: `androidx.compose.ui.test.junit4.v2.createComposeRule`)
- `DiaryTheme`으로 감싼다.
- 메서드명은 백틱(`` ` ``)으로 감싸 한글로 작성한다. (예: `` fun `초기 텍스트가 표시된다`() ``)

#### 공통

- 테스트 데이터는 직접 생성하지 않고 MockK 또는 FixtureMonkey를 사용한다.
- FixtureMonkey는 `giveMeKotlinBuilder<T>().set(...).sample()` 패턴을 사용한다.
- 테스트에 필요한 필드만 직접 지정한다.
- 같은 모듈 내 기존 테스트 파일의 패턴을 따른다.
