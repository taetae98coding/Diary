package com.taetae98.diary.domain.usecase.memo.tag

import com.taetae98.diary.domain.entity.account.Account
import com.taetae98.diary.domain.entity.memo.MemoTag
import com.taetae98.diary.domain.repository.MemoTagFireStoreRepository
import com.taetae98.diary.domain.repository.MemoTagRepository
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

class DeleteMemoTagUseCaseBddTest : BehaviorSpec(
    {
        val account = mockk<Account>(relaxed = true)
        val getAccountUseCase = mockk<GetAccountUseCase>()
        val memoTagFireStoreRepository = mockk<MemoTagFireStoreRepository>()
        val memoTagRepository = mockk<MemoTagRepository>()
        val useCase = DeleteMemoTagUseCase(
            getAccountUseCase = getAccountUseCase,
            memoTagRepository = memoTagRepository,
            memoTagFireStoreRepository = memoTagFireStoreRepository,
        )

        every { getAccountUseCase(Unit) } returns flowOf(Result.success(account))

        Given("유효하지 않은 MemoTag가 주어졌을 때") {
            val memoTag = mockk<MemoTag> {
                every { isInvalid() } returns true
            }

            When("UseCase를 호출하면") {
                val result = useCase(memoTag)

                Then("성공한다.") {
                    result.shouldBeSuccess()
                }

                Then("delete를 호출하지 않는다.") {
                    coVerify(exactly = 0) { memoTagRepository.delete(memoTag) }
                }
            }
        }

        Given("유효한 MemoTag가 주어졌을 때") {
            val memoTag = mockk<MemoTag> {
                every { isInvalid() } returns false
            }

            coEvery { memoTagRepository.delete(memoTag) } returns Unit

            When("UseCase를 호출하면") {
                val result = useCase(memoTag)

                Then("성공한다.") {
                    result.shouldBeSuccess()
                }

                Then("delete를 호출한다.") {
                    coVerify(exactly = 1) { memoTagRepository.delete(memoTag) }
                }
            }
        }
    },
)