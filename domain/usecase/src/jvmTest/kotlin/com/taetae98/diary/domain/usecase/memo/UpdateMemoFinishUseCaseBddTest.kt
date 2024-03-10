package com.taetae98.diary.domain.usecase.memo

import com.taetae98.diary.domain.entity.account.Account
import com.taetae98.diary.domain.repository.MemoFireStoreRepository
import com.taetae98.diary.domain.repository.MemoRepository
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

class UpdateMemoFinishUseCaseBddTest : BehaviorSpec(
    {
        val getAccountUseCase = mockk<GetAccountUseCase>()
        val memoRepository = mockk<MemoRepository>(relaxed = true, relaxUnitFun = true)
        val memoFireStoreRepository = mockk<MemoFireStoreRepository>(relaxed = true, relaxUnitFun = true)
        val useCase = UpdateMemoFinishUseCase(
            getAccountUseCase = getAccountUseCase,
            memoRepository = memoRepository,
            memoFireStoreRepository = memoFireStoreRepository,
        )

        val account = mockk<Account>()
        every { getAccountUseCase(Unit) } returns flowOf(Result.success(account))

        Given("올바르지 않은 Params가 들어올 때") {
            val params = mockk<UpdateMemoFinishUseCase.Params> {
                every { isInvalid() } returns true
            }

            When("UseCase를 호출하면") {
                val result = useCase(params)

                Then("무시하기 때문에 성공한다.") {
                    result.shouldBeSuccess()
                }

                Then("무시하기 때문에 upsert를 호출하지 않는다.") {
                    coVerify(exactly = 0) { memoRepository.updateFinish(any(), any()) }
                    coVerify(exactly = 0) { memoFireStoreRepository.updateFinish(any(), any()) }
                }

                clearAllMocks(answers = false)
            }
        }

        Given("올바른 Params가 들어올 때") {
            val params = mockk<UpdateMemoFinishUseCase.Params>(relaxed = true) {
                every { isInvalid() } returns false
            }

            And("그리고 로그인이 안됐을 때") {
                every { account.isLogin } returns false

                When("UseCase를 호출하면") {
                    val result = useCase(params)

                    Then("성공한다.") {
                        result.shouldBeSuccess()
                    }

                    Then("MemoRepository.updateFinish 1회 호출한다.") {
                        coVerify(exactly = 1) { memoRepository.updateFinish(any(), any()) }
                    }

                    Then("MemoFireRepository.updateFinish 0회 호출한다.") {
                        coVerify(exactly = 0) { memoFireStoreRepository.updateFinish(any(), any()) }
                    }

                    clearAllMocks(answers = false)
                }
            }

            And("그리고 로그인 됐을 때") {
                every { account.isLogin } returns true

                When("UseCase를 호출하면") {
                    val result = useCase(params)

                    Then("성공한다.") {
                        result.shouldBeSuccess()
                    }

                    Then("MemoRepository.updateFinish 1회 호출한다.") {
                        coVerify(exactly = 1) { memoRepository.updateFinish(any(), any()) }
                    }

                    Then("MemoFireRepository.updateFinish 1회 호출한다.") {
                        coVerify(exactly = 1) { memoFireStoreRepository.updateFinish(any(), any()) }
                    }

                    clearAllMocks(answers = false)
                }
            }
        }
    },
)
