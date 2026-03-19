# Presenter

:client:presenter 모듈은 :client:feature 모듈에서 공통으로 사용하는 코드를 작성한다.

## 지침

- api 모듈은 interface로 `***StateHolder`를 만든다.
- impl 모듈은 `***StateHolderImpl`을 만들고 Compose에서 StateHolder 로직을 구현한다.
- 만약 StateHolder 내부 로직은 같지만 :feature 마다 UseCase를 다르게 호출하는 경우. api 모듈에 `***Strategy` 추가하고 feature 모듈에 구현하여 주입한다. 이 경우 api 모듈에 StateHolder를 class로 만드는 것을 허용한다.
