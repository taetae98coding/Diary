package com.taetae98.diary.domain.usecase.memo

import com.taetae98.diary.domain.entity.account.Account
import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.entity.memo.MemoId
import com.taetae98.diary.domain.repository.MemoFireStoreRepository
import com.taetae98.diary.domain.repository.MemoRepository
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

class DeleteMemoUseCaseBddTest : BehaviorSpec(
    {
        val account = mockk<Account>()
        val getAccountUseCase = mockk<GetAccountUseCase>()
        val memoRepository = mockk<MemoRepository>(relaxed = true, relaxUnitFun = true)
        val memoFireStoreRepository = mockk<MemoFireStoreRepository>(relaxed = true, relaxUnitFun = true)
        val useCase = DeleteMemoUseCase(
            getAccountUseCase = getAccountUseCase,
            memoRepository = memoRepository,
            memoFireStoreRepository = memoFireStoreRepository,
        )

        every { getAccountUseCase(Unit) } returns flowOf(Result.success(account))

        Given("유효한 id가 주어질때") {
            val memoId = MemoId("value").also { it.isInvalid().shouldBeFalse() }
            val memo = mockk<Memo> { every { id } returns memoId.value }

            coEvery { memoRepository.delete(memoId.value) } returns memo

            And("로그인이 안됐을 때") {
                every { account.isLogin } returns false

                When("UseCase를 호출하면") {
                    val result = useCase(memoId)

                    Then("성공한다.") {
                        result.shouldBeSuccess()
                    }

                    Then("id가 동일한 memo를 리턴한다.") {
                        result.getOrNull()?.id?.shouldBe(memoId.value)
                    }

                    Then("delete가 1회 호출된다.") {
                        coVerify(exactly = 1) { memoRepository.delete(any()) }
                    }

                    Then("FireStore delete 0회 호출된다.") {
                        coVerify(exactly = 0) { memoFireStoreRepository.delete(any()) }
                    }
                }

                clearAllMocks(answers = false)
            }

            And("로그인이 됐을 때") {
                every { account.isLogin } returns true

                When("UseCase를 호출하면") {
                    val result = useCase(memoId)

                    Then("성공한다.") {
                        result.shouldBeSuccess()
                    }

                    Then("id가 동일한 memo를 리턴한다.") {
                        result.getOrNull()?.id?.shouldBe(memoId.value)
                    }

                    Then("delete가 1회 호출된다.") {
                        coVerify(exactly = 1) { memoRepository.delete(any()) }
                    }

                    Then("FireStore delete 1회 호출된다.") {
                        coVerify(exactly = 1) { memoFireStoreRepository.delete(any()) }
                    }
                }

                clearAllMocks(answers = false)
            }
        }

        Given("유효하지 않은 id가 주어지고") {
            val id = MemoId("").also { it.isInvalid().shouldBeTrue() }

            When("UseCase를 호출하면") {
                val result = useCase(id)

                Then("null을 리턴한다.") {
                    result.shouldBeSuccess(null)
                }

                Then("delete가 호출되지 않는다.") {
                    coVerify(exactly = 0) { memoRepository.delete(any()) }
                }
            }

            clearAllMocks(recordedCalls = true)
        }
    },
)
