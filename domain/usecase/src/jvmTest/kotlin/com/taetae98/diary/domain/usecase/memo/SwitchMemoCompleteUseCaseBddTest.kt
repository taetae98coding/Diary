package com.taetae98.diary.domain.usecase.memo

import io.kotest.core.spec.style.BehaviorSpec

class SwitchMemoCompleteUseCaseBddTest : BehaviorSpec({
    Given("Complete Memo가 주어졌을 때") {
        When("useCase를 호출하면") {
            Then("incompleteMemoUseCase 1회 호출한다.") {
                // TODO
            }

            Then("completeMemoUseCase 0회 호출한다.") {
                // TODO
            }
        }
    }

    Given("Incomplete Memo가 주어졌을 때") {
        When("useCase를 호출하면") {
            Then("incompleteMemoUseCase 0회 호출한다.") {
                // TODO
            }

            Then("completeMemoUseCase 1회 호출한다.") {
                // TODO
            }
        }
    }
})