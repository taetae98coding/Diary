package com.taetae98.diary.domain.usecase.memo

import com.taetae98.diary.domain.entity.account.Account
import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.exception.TitleEmptyException
import com.taetae98.diary.domain.repository.MemoFireStoreRepository
import com.taetae98.diary.domain.repository.MemoRepository
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

class UpsertMemoUseCaseBddTest : BehaviorSpec(
    {
        val getAccountUseCase = mockk<GetAccountUseCase>()
        val memoRepository = mockk<MemoRepository>(relaxed = true, relaxUnitFun = true)
        val memoFioreStoreRepository = mockk<MemoFireStoreRepository>(relaxed = true, relaxUnitFun = true)
        val useCase = UpsertMemoUseCase(
            getAccountUseCase = getAccountUseCase,
            memoRepository = memoRepository,
            memoFireStoreRepository = memoFioreStoreRepository,
        )
        val memo = mockk<Memo>()
        val account = mockk<Account>()

        every { getAccountUseCase(Unit) } returns flowOf(Result.success(account))

        Given("Title이 Empty인 Memo가 주어졌을 때") {
            every { memo.title } returns ""

            When("UseCase를 호출하면") {
                val result = useCase(memo)

                Then("TitleEmptyException 발생한다.") {
                    result.shouldBeFailure<TitleEmptyException>()
                }

                Then("Upsert 호출하지 않는다.") {
                    coVerify(exactly = 0) { memoRepository.upsert(any()) }
                    coVerify(exactly = 0) { memoFioreStoreRepository.upsert(any()) }
                }
            }
        }

        Given("Title이 문자로 이루어진 Memo가 주어지고") {
            every { memo.title } returns "asdf"

            And("isLogin false일 때") {
                every { account.isLogin } returns false

                When("UseCase를 호출하면") {
                    val result = useCase(memo)

                    Then("통과한다.") {
                        result.shouldBeSuccess()
                    }

                    Then("MemoRepository.upsert 호출한다.") {
                        coVerify(exactly = 1) { memoRepository.upsert(memo) }
                    }

                    Then("로그인하지 않았으니 FireStore.upsert 호출하지 않는다.") {
                        coVerify(exactly = 0) { memoFioreStoreRepository.upsert(memo) }
                    }
                }

                clearAllMocks(answers = false)
            }

            And("isLogin true일 때") {
                every { account.isLogin } returns true

                When("UseCase를 호출하면") {
                    val result = useCase(memo)

                    Then("통과한다.") {
                        result.shouldBeSuccess()
                    }

                    Then("MemoRepository, FireStoreRepository upsert 1회씩 호출한다.") {
                        coVerify(exactly = 1) { memoRepository.upsert(memo) }
                        coVerify(exactly = 1) { memoFioreStoreRepository.upsert(memo) }
                    }
                }

                clearAllMocks(answers = false)
            }
        }
    },
)
