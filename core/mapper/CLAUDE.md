# Client Core Mapper

## 지침

- Mapper 함수 순서는 아래처럼 작성한다.
  1. Domain to Local
  2. Domain to Remote
  3. Local to Domain
  4. Local to Remote
  5. Remote to Domain
  6. Remote to Local

## 테스트

- 테스트 이름은 간결하게 작성한다.
  - Domain to Local
  - Local to Domain
  - Remote to Domain
- TC 순서는 Mapper 함수 순서와 동일하게 작성한다.
