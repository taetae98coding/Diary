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

## 빌드 명령어

```bash
./gradlew :app:android:assembleDebug                      # 빌드
./gradlew :<module-path>:jvmTest --tests "<FQCN>"         # Kotest
./gradlew :<module-path>:allSdksGroupAndroidDeviceTest \    # Android 디바이스 테스트
  -Pandroid.testInstrumentationRunnerArguments.class=<FQCN>
./gradlew --no-configuration-cache spotlessCheck           # 포맷 검사
./gradlew projectGuardCheck                               # 의존성 가드
```
