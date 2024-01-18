package com.taetae98.diary.domain.usecase.memo

import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.entity.memo.MemoState
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

class SwitchMemoCompleteUseCaseBddTest : BehaviorSpec({
    val completeMemoId = "completeMemoId"
    val completeMemo = mockk<Memo>(relaxed = true, relaxUnitFun = true) {
        every { id } returns completeMemoId
        every { state } returns MemoState.COMPLETE
    }
    val incompleteMemoId = "incompleteMemoId"
    val incompleteMemo = mockk<Memo>(relaxed = true, relaxUnitFun = true) {
        every { id } returns incompleteMemoId
        every { state } returns MemoState.INCOMPLETE
    }
    val findByIdMemoUseCase = mockk<FindByIdMemoUseCase>(relaxed = true, relaxUnitFun = true) {
        every { this@mockk.invoke(completeMemoId) } returns flowOf(Result.success(completeMemo))
        every { this@mockk.invoke(incompleteMemoId) } returns flowOf(Result.success(incompleteMemo))
    }
    val completeMemoUseCase = mockk<CompleteMemoUseCase>(relaxed = true, relaxUnitFun = true)
    val incompleteMemoUseCase = mockk<IncompleteMemoUseCase>(relaxed = true, relaxUnitFun = true)
    val useCase = SwitchMemoCompleteUseCase(
        findByIdMemoUseCase = findByIdMemoUseCase,
        completeMemoUseCase = completeMemoUseCase,
        incompleteMemoUseCase = incompleteMemoUseCase,
    )

    Given("Complete Memo가 주어졌을 때") {
        When("useCase를 호출하면") {
            useCase(completeMemoId)

            Then("incompleteMemoUseCase 1회 호출한다.") {
                coVerify(exactly = 1) { incompleteMemoUseCase(completeMemoId) }
            }

            Then("completeMemoUseCase 0회 호출한다.") {
                coVerify(exactly = 0) { completeMemoUseCase(completeMemoId) }
            }
        }
    }

    Given("Incomplete Memo가 주어졌을 때") {
        When("useCase를 호출하면") {
            useCase(incompleteMemoId)

            Then("incompleteMemoUseCase 0회 호출한다.") {
                coVerify(exactly = 0) { incompleteMemoUseCase(incompleteMemoId) }
            }

            Then("completeMemoUseCase 1회 호출한다.") {
                coVerify(exactly = 1) { completeMemoUseCase(incompleteMemoId) }
            }
        }
    }
})