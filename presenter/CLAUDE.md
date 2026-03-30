# Presenter

:presenter 모듈은 :feature 모듈에서 공통으로 사용하는 코드를 작성한다.

## 지침

- `***StateHolder`를 만든다.
- Compose에서 StateHolder를 사용하는 Scaffold UI를 구현한다.
- 만약 StateHolder 내부 로직은 같지만 :feature 마다 UseCase를 다르게 호출하는 경우. `***Strategy`를 추가하고 feature 모듈에 구현하여 주입한다.
