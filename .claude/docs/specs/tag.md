# Tag Spec

> 상태: 작성 중

## 1. 개요

Tag는 **Memo를 분류·구성하기 위한 라벨**을 표현하는 도메인이다. 이름/설명/색상만 가진 얇은 메타데이터 컨테이너다.

- Memo는 `primaryTag`로 단일 Tag를 참조한다.
- Tag 자체는 어떤 Memo와 연결돼 있는지 알지 못한다. 관계는 Memo 측에 있다.

## 2. 설계 원칙

### Soft Delete

Tag는 물리적으로 삭제하지 않고 `isDeleted` 플래그로만 논리 삭제한다. 이 태그를 참조 중인 Memo의 무결성 유지와 복원/동기화 목적이다.

### 완료는 되돌릴 수 있는 상태

Tag는 `isFinished` 상태를 가지며 **재시작(restart)** 을 통해 완료를 되돌릴 수 있다. 일회용/기간 한정 태그 사용을 지원한다. 완료는 삭제와 다른 축이며, 완료된 태그도 여전히 참조 가능하다.

## 3. 핵심 개념

| 용어 | 구분 | 의미 |
|---|---|---|
| **isFinished** | 앱 특화 | Tag의 완료 상태. 재시작으로 되돌릴 수 있다. |
| **Restart** | 앱 특화 | 완료된 Tag를 다시 미완료로 전환하는 동작. `RestartTagUseCase`. |

## 4. 참고

- 관련 모듈
  - 모델: `core/model` (패키지 `...core.model.tag`)
  - 도메인/데이터: `domain/tag`, `data/tag`
  - UI: `feature/tag`, `presenter/tag`
