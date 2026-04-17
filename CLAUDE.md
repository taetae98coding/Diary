# Diary

Kotlin Multiplatform 일기/메모 앱 (Android, iOS, JVM, WASM).

## 지침

- TradeOff가 있는 선택지는 개발자에게 장단점을 비교하여 설명한다.

## 참고 프로젝트

1. [nowinandroid](https://github.com/android/nowinandroid)
2. [Droidkaigi](https://github.com/DroidKaigi/conference-app-2025)

## 레이어 의존 방향

`domain` ← `data` ← `feature` ← `app`

ProjectGuard로 강제한다. domain은 data/feature를 알 수 없고, feature는 data를 직접 참조할 수 없다.

## 공통 컨벤션

- Clock 주입: @.claude/docs/clock-convention.md
- Compose 컨벤션: @.claude/docs/compose-conventions.md

## 도메인 스펙

`.claude/docs/specs/` 에 도메인별 스펙 문서가 있다. 도메인 관련 작업 전 해당 스펙을 먼저 확인한다.

## 빌드 명령어

```bash
./gradlew :app:android:assembleDebug                        # 빌드
./gradlew :<module-path>:jvmTest --tests "<FQCN>"           # Kotest
./gradlew :<module-path>:allSdksGroupAndroidDeviceTest \     # Android 디바이스 테스트
  -Pandroid.testInstrumentationRunnerArguments.class=<FQCN>
./gradlew --no-configuration-cache spotlessCheck            # 포맷 검사
./gradlew spotlessApply                                     # 포맷 자동 수정
./gradlew projectGuardCheck                                 # 의존성 가드
```

## 작업 완료 체크리스트

코드 변경 후 다음 순서로 검증한다:

1. `./gradlew spotlessApply` — 포맷 자동 수정
2. `./gradlew projectGuardCheck` — 레이어 의존성 위반 확인
3. 해당 모듈 테스트 실행 (`jvmTest` 또는 `allSdksGroupAndroidDeviceTest`)
