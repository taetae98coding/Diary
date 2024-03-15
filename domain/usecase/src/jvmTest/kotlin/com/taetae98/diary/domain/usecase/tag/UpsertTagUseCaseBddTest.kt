package com.taetae98.diary.domain.usecase.tag

import com.taetae98.diary.domain.entity.account.Account
import com.taetae98.diary.domain.entity.tag.Tag
import com.taetae98.diary.domain.exception.TitleEmptyException
import com.taetae98.diary.domain.repository.TagFireStoreRepository
import com.taetae98.diary.domain.repository.TagRepository
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

class UpsertTagUseCaseBddTest : BehaviorSpec(
    {
        val account = mockk<Account>()
        val getAccountUseCase = mockk<GetAccountUseCase>()
        val tagRepository = mockk<TagRepository>(relaxed = true, relaxUnitFun = true)
        val tagFireStoreRepository = mockk<TagFireStoreRepository>(relaxed = true, relaxUnitFun = true)
        val useCase = UpsertTagUseCase(
            getAccountUseCase = getAccountUseCase,
            tagRepository = tagRepository,
            tagFireStoreRepository = tagFireStoreRepository,
        )

        every { getAccountUseCase(Unit) } returns flowOf(Result.success(account))

        Given("Title이 Empty인 Tag가 주어졌을 때") {
            val tag = mockk<Tag> {
                every { title } returns ""
            }

            When("UseCase를 호출하면") {
                val result = useCase(tag)

                Then("TitleEmptyException 발생한다.") {
                    result.shouldBeFailure<TitleEmptyException>()
                }
            }
        }

        Given("Title이 문자로 이루어진 Tag가 주어졌을 때") {
            val tag = mockk<Tag> {
                every { title } returns "asdf"
            }

            And("로그인 상태일 때") {
                every { account.isLogin } returns true

                When("UseCase를 호출하면") {
                    val result = useCase(tag)

                    Then("통과한다.") {
                        result.shouldBeSuccess()
                    }

                    Then("FireStore 호출한다.") {
                        coVerify(exactly = 1) { tagFireStoreRepository.upsert(any()) }
                    }

                    clearAllMocks(answers = false)
                }
            }

            And("로그아웃 상태일 때") {
                every { account.isLogin } returns false

                When("UseCase를 호출하면") {
                    val result = useCase(tag)

                    Then("통과한다.") {
                        result.shouldBeSuccess()
                    }

                    Then("FireStore 호출하지 않는다.") {
                        coVerify(exactly = 0) { tagFireStoreRepository.upsert(any()) }
                    }

                    clearAllMocks(answers = false)
                }
            }
        }
    },
)
