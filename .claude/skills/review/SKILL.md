---
name: review
description: ":client 하위 모듈의 코드를 리뷰한다."
argument-hint: [대상 파일 또는 컴포넌트 이름]
allowed-tools: Read, Grep, Glob, Agent
---

:client 하위 모듈의 코드를 리뷰한다.

대상: `$ARGUMENTS`

## 리뷰 항목

- Android Developer 문서 권장사항을 지키는지 확인한다.
- 참고 프로젝트(nowinandroid, Droidkaigi)와 유사한 구조인지 확인한다.
- 성능 이슈가 없는지 확인한다.
- Compose 코드인 경우 recomposition 최적화가 잘 됐는지 확인한다.
- Compose 코드인 경우 remember 처리가 필요한 연산이 있는지 확인한다.

## 논이슈 (리뷰 대상에서 제외)

- 실험적 API 사용
- 한국어 하드코딩
