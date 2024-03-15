package com.taetae98.diary.domain.usecase.tag

import androidx.paging.PagingData
import com.taetae98.diary.domain.entity.account.Account
import com.taetae98.diary.domain.exception.NoAccountException
import com.taetae98.diary.domain.repository.TagRepository
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class PageTagUseCaseBddTest : BehaviorSpec(
    {
        val getAccountUseCase = mockk<GetAccountUseCase>()
        val tagRepository = mockk<TagRepository>()
        val useCase = PageTagUseCase(
            getAccountUseCase = getAccountUseCase,
            tagRepository = tagRepository,
        )

        Given("Account를 불러올 수 없을 때") {
            every { getAccountUseCase(Unit) } returns flowOf(Result.failure(Exception()))

            When("UseCase를 호출하면") {
                val result = useCase(Unit).first()

                Then("실패한다.") {
                    result.shouldBeFailure<NoAccountException>()
                }

                Then("page 함수를 호출하지 않는다.") {
                    verify(exactly = 0) { tagRepository.page(any()) }
                }
            }
        }

        Given("Account를 불러올 수 있을 때") {
            val account = mockk<Account>(relaxed = true)

            every { getAccountUseCase(Unit) } returns flowOf(Result.success(account))
            every { tagRepository.page(account.uid) } returns flowOf(PagingData.empty())

            When("UseCase를 호출하면") {
                val result = useCase(Unit).first()

                Then("성공한다.") {
                    result.shouldBeSuccess()
                }

                Then("page 함수를 1회 호출한다.") {
                    verify(exactly = 1) { tagRepository.page(account.uid) }
                }
            }
        }
    },
)