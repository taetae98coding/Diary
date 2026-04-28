# Memo Spec

> 상태: 작성 중

## 1. 개요

Memo는 **단발성 일정/노트**를 표현하는 도메인이다. 반복되는 일정은 `Routine`이 담당한다.

- 각 Memo는 제목/설명/시간 범위/색상으로 구성된다. 시간 범위(`localDateTimeRange`)는 **nullable**이며, 시간 정보 없는 메모를 허용한다.
- 단일 `primaryTag`로 분류할 수 있다 (선택적). 다중 태그 연결은 범위 밖이다.
- 완료 상태(`isFinished`)와 논리 삭제(`isDeleted`)를 독립적으로 추적한다.

## 2. 설계 원칙

### Soft Delete

Memo는 물리적으로 삭제하지 않고 `isDeleted` 플래그로만 논리 삭제한다. 복원과 동기화 충돌 회피가 목적이다.

### 완료와 삭제는 독립 상태

`isFinished`와 `isDeleted`는 서로 독립된 축이다. 완료된 메모를 삭제하거나 삭제된 메모가 다시 복원되는 경로는 각각의 상태 전이로 다룬다.

### 캘린더 뷰는 전용 모델 사용

전체 `Memo`가 아닌 경량 `CalendarMemo`를 조회 모델로 사용한다. 캘린더 렌더링 성능 확보가 목적이며, 상세 정보가 필요한 시점에만 전체 Memo를 로드한다.

`CalendarMemo`의 `localDateTimeRange`는 **non-null**이다. 시간 범위가 없는 Memo는 캘린더 뷰 대상에서 제외된다 (`MemoDetail.localDateTimeRange`는 nullable인 것과 비대칭).

## 3. 핵심 개념

| 용어 | 구분 | 의미 |
|---|---|---|
| **primaryTag** | 앱 특화 | Memo가 참조하는 단일 Tag ID. 선택적. 관계는 Memo 측에서만 표현된다. |
| **isAllDay** | 앱 특화 | 특정 시간대 없이 하루 전체로 표시할지 여부. |
| **CalendarMemo** | 앱 특화 | 캘린더 뷰 전용 간소 모델 (제목/시간/색상 등 표시 최소 정보). |
| **FilterPresence** | 앱 특화 | 목록 필터에서 특정 조건(tag/date)의 포함 여부. `NONE` / `YES` / `NO` 삼태(tri-state). |

## 4. 참고

- 관련 모듈
  - 모델: `core/model` (패키지 `...core.model.memo`)
  - 도메인/데이터: `domain/memo`, `data/memo`
  - UI: `feature/memo`, `presenter/memo`
  - 클라이언트(서버 연동): `client/feature/memo`, `client/presenter/memo`
