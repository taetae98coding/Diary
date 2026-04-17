---
name: diary-prd
description: |-
  새로운 기능/화면의 PRD(Product Requirements Document) 작성 요청 시 실행한다.
  TRIGGER when: "PRD 작성해줘", "PRD 만들어줘", "스펙 문서 작성", "새 기능 기획", "기능 설계해줘" 류 요청; 신규 도메인/화면의 요구사항 정리 요청.
  SKIP: 이미 작성된 PRD 리뷰/수정; 코드 구현 요청("구현해줘"); 버그 수정; "어떻게 만들지?" 같은 단순 구현 방법 질문; 코드 리뷰.
argument-hint: [기능 이름 또는 요구사항 요약]
allowed-tools: Read, Grep, Glob, Write, Agent
---

# PRD 작성 워크플로우

대상: `$ARGUMENTS`

## 아키텍처

이 skill은 **대화형 오케스트레이터**다. 무거운 조사 단계는 전용 subagent로 위임하고, 사용자 대화와 문서 종합은 skill이 담당한다.

- `diary-prd-codebase-scout` — 기존 코드베이스 조사 (Read/Glob/Grep)
- `diary-prd-market-scout` — 유사 서비스 / Best Practice 조사 (WebSearch/WebFetch)

기능 규모에 따라 섹션을 **선택적으로 포함**한다 (Small / Medium / Large).

## 절차

### 1단계: 요구사항 명확화 [🟢 Skill — 사용자 대화]

- 사용자에게 기능의 **목적 · 범위 · 비범위 · 핵심 사용자 시나리오**를 확인한다.
- 불명확한 부분은 질문하여 명확화한 뒤 다음 단계로 진행한다.
- 기능 규모 추정 (Small / Medium / Large).
  - **Small** (UI 변경, 기존 도메인 소규모 확장): 2b(market-scout) 생략 가능.
  - **Medium / Large**: 2a, 2b 모두 실행.

### 2단계: 병렬 조사 [🔵 Subagent 위임]

**하나의 메시지 안에서 두 `Agent` 툴 호출을 병렬 실행한다.**

#### 2a. `diary-prd-codebase-scout`

프롬프트 템플릿:
```
기능 개요: <1단계에서 파악한 요구사항>

이 기능의 PRD 작성을 위해 Diary 코드베이스를 조사해줘.

조사 대상:
- 관련 도메인 스펙 (.claude/docs/specs/)
- 유사 기능 모듈 (domain, data, feature)
- 재사용 가능한 UI 컴포넌트 (파일 경로:라인 포함)
- 데이터 모델 영향 (FK, 마이그레이션)
- 적용 대상 컨벤션 (Clock, Compose, KMP)

출력은 지정된 구조화 보고서 형식을 준수해줘.
```

#### 2b. `diary-prd-market-scout`

프롬프트 템플릿:
```
기능 개요: <1단계에서 파악한 요구사항>

이 기능의 PRD 작성을 위해 유사 서비스와 업계 Best Practice를 조사해줘.

조사 축:
- UX 패턴
- 데이터 모델
- 동기화 / 오프라인 전략
- 피해야 할 함정

출력은 지정된 구조화 보고서 형식을 준수해줘.
```

### 3단계: 조사 요약 & 방향 확정 [🟢 Skill — 사용자 대화]

두 subagent 보고서를 종합하여 사용자에게 제시한다. 특히:

- **재사용 가능 컴포넌트** vs **신규 구현 필요 항목** (경로 포함)
- 채택할 패턴 / 피해야 할 함정
- 발견된 제약과 트레이드오프

사용자 피드백으로 설계 방향을 확정하고 4단계로 진행한다.

### 4단계: PRD 초안 작성 [🟢 Skill]

아래 구조로 작성한다. **굵은 섹션은 필수**, 나머지는 규모에 따라 선택.

#### 4.1. **개요** (필수)
- 목적 / 범위 / 비범위

#### 4.2. **사용자 시나리오** (필수)
- 골든 패스 / 엣지 케이스

#### 4.3. 유사 서비스 조사 결과 (Medium+)
- market-scout 결과 반영
- 참고 서비스와 채택/배제 이유

#### 4.4. **도메인 모델** (필수)
- 엔티티 / 필드 / 타입
- FK / 관계 설계 (SQL JOIN 우선)
- DB 마이그레이션 영향

#### 4.5. **레이어 설계** (필수)
- `:domain` — UseCase (행위 중심), Repository 인터페이스 (CRUD 중심)
- `:data` — DataSource, DTO, 로컬/원격 구현
- `:feature` — Screen, Presenter/ViewModel, State
- 의존 방향 (`domain ← data ← feature`) 검증

#### 4.6. **UI 컴포넌트 재사용 vs 신규** (필수)
- codebase-scout 결과 반영
- 재사용 가능 컴포넌트 목록 (경로 포함)
- 신규 컴포넌트 근거 및 Compose 컨벤션 준수안

#### 4.7. 플랫폼별 제약 (Medium+)
- Android / iOS / JVM / WASM 지원 범위
- 플랫폼별 제약 사항
- `expect`/`actual` 필요 여부

#### 4.8. 동기화 / 오프라인 전략 (Medium+)
- 오프라인 동작 정의
- 동기화 실패 시 UX (기존 Snackbar 패턴 등)
- 충돌 해결

#### 4.9. **엣지 케이스 / 에러 시나리오** (필수)
- 빈 상태, 네트워크 실패, 권한 거부, 동시성 등
- 각 시나리오의 UX 정의

#### 4.10. **TC 설계** (필수)
- **UseCase TC** — Kotest `BehaviorSpec`, Given/When/Then 목록
- **Mapper TC** — 커버리지 100%
- **Compose TC** — JUnit4, 핵심 상태/인터랙션 목록
- 테스트 환경 결정 (Compose 모듈 → `androidDeviceTest`, 그 외 → `jvmTest`)
- Clock / ID 생성기 등 side-input 주입 계획

#### 4.11. **구현 Phase** (필수)

PRD는 구현 Phase 분해로 마무리한다. 각 Phase는 **단독으로 빌드·테스트·머지 가능**해야 하며, 머지 후 앱은 항상 green을 유지한다 (컴파일·ProjectGuard·기존 테스트 통과, 가능하면 실기기 구동).

각 Phase는 아래 형식으로 기술한다:

- **목표** — 이 Phase가 완료되었을 때 사용자/개발자 관점에서 달성되는 것 (1–2문장)
- **산출물** — 생성/수정되는 파일 목록. 신규 파일은 전체 경로, 기존 파일은 `file_path` + 변경 요약
- **검증** — 실행할 Gradle 명령 블록. 실기기/에뮬레이터 확인이 필요하면 "실기기 스모크:" 항목으로 분리
- **의존** — 이전 Phase 번호 목록. 없으면 "없음"

또한 PRD 상단에 **Phase 의존 그래프**를 ASCII로 그린다. 병렬 가능한 Phase를 시각화한다.

Phase 분해 원칙:
- **"1 PR 수준"** 크기. 너무 크면 더 쪼갠다 (예: 10개 넘는 신규 파일은 분할 고려)
- 첫 Phase는 주로 **모듈/뼈대 생성** (settings.gradle.kts 등록, 빈 모듈, UseCase 시그니처)
- UI 공통 컴포넌트 신규 구현은 별도 Phase로 분리 (재사용성·테스트 독립성)
- 스크린 **조립**은 마지막 Phase (재료 Phase들이 완료된 뒤)
- 머지 순서 상 아직 없는 의존은 **Placeholder**로 대체하고 후속 Phase에서 교체 (예: 빈 Add 화면 → Phase 5에서 대체)
- 각 Phase 검증 섹션에는 항상 `spotlessApply`, `projectGuardCheck`, 관련 테스트 명령을 포함

### 5단계: 피드백 및 반영 [🟢 Skill — 사용자 대화]

- 초안을 사용자에게 제시하고 피드백을 받는다.
- 특히 4.3, 4.6, 4.10, **4.11** 섹션에 대한 확인을 요청한다.
- 피드백 반영 후 재검토한다.

### 6단계: 저장 [🟢 Skill]

- 확정된 PRD는 `.claude/docs/prd/<feature-name>.md`에 저장한다.
- 도메인 개념 스펙(`.claude/docs/specs/<domain>.md`)과 구분한다 — PRD는 구현 계획, specs는 도메인 개념.

## 작성 규칙

- **코드 변경 금지** — PRD 문서 생성까지가 범위다.
- Subagent 보고서를 그대로 복사하지 않고 **종합하여** PRD에 반영한다.
- 추측은 `확인 필요`로 명시한다.
- 기존 파일/클래스 참조는 `file_path:line_number` 형식 사용.
- **접근성 / i18n 섹션은 작성하지 않는다.**
- **Phase 분리는 생략하지 않는다** — Small 규모라도 최소 1~2개 Phase로 기술한다.
