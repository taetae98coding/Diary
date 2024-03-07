package com.taetae98.diary.domain.usecase.memo

import com.taetae98.diary.domain.repository.MemoRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk

class UpdateMemoFinishUseCaseBddTest : BehaviorSpec(
    {
        val memoRepository = mockk<MemoRepository>()
        val useCase = UpdateMemoFinishUseCase(memoRepository = memoRepository)

        Given("올바르지 않은 Params가 들어올 때") {
            val params = mockk<UpdateMemoFinishUseCase.Params> {
                every { isInvalid() } returns true
            }

            When("UseCase를 호출하면") {
                val result = useCase(params)

                Then("무시하기 때문에 성공한다.") {
                    result.shouldBeSuccess()
                }

                Then("무시하기 때문에 updateFinish 호출하지 않는다.") {
                    coVerify(exactly = 0) { memoRepository.updateFinish(params.memoId, params.isFinish) }
                }
            }

            clearAllMocks(recordedCalls = true)
        }

        Given("올바른 Params가 들어올 때") {
            val params = mockk<UpdateMemoFinishUseCase.Params>(relaxed = true) {
                every { isInvalid() } returns false
            }

            When("UseCase를 호출하면") {
                useCase(params)

                Then("updateFinish 호출한다.") {
                    coVerify(exactly = 1) { memoRepository.updateFinish(params.memoId, params.isFinish) }
                }
            }
        }
    },
)
