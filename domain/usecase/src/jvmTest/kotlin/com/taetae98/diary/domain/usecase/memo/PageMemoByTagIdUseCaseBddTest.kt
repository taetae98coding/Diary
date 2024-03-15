package com.taetae98.diary.domain.usecase.memo

import androidx.paging.PagingData
import com.taetae98.diary.domain.entity.account.Account
import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.entity.tag.TagId
import com.taetae98.diary.domain.exception.NoAccountException
import com.taetae98.diary.domain.repository.MemoRepository
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class PageMemoByTagIdUseCaseBddTest : BehaviorSpec({
    val getAccountUseCase = mockk<GetAccountUseCase>()
    val memoRepository = mockk<MemoRepository>()
    val useCase = PageMemoByTagIdUseCase(
        getAccountUseCase = getAccountUseCase,
        memoRepository = memoRepository,
    )

    Given("TagId가 유효하지 않을 때") {
        val tagId = TagId("").also { it.isInvalid().shouldBeTrue() }

        When("UseCase를 호출하면") {
            val result = useCase(tagId).first()

            Then("성공한다.") {
                result.shouldBeSuccess()
            }
        }
    }

    Given("Account를 불러올 수 없을 때") {
        val tagId = TagId("valid").also { it.isInvalid().shouldBeFalse() }

        every { getAccountUseCase(Unit) } returns flowOf(Result.failure(Exception()))

        When("UseCase를 호출하면") {
            val result = useCase(tagId).first()

            Then("실패한다.") {
                result.shouldBeFailure<NoAccountException>()
            }
        }

        clearMocks(getAccountUseCase)
    }

    Given("TagId가 정상이고, Account를 불러올 수 있을 때") {
        val account = Account.Guest
        val tagId = TagId("valid").also { it.isInvalid().shouldBeFalse() }
        val pagingData = PagingData.empty<Memo>()

        every { getAccountUseCase(Unit) } returns flowOf(Result.success(account))
        every { memoRepository.page(account.uid, tagId.value) } returns flowOf(pagingData)

        When("UseCase를 호출하면") {
            val result = useCase(tagId)

            Then("성공한다.") {
                result.first().shouldBeSuccess(pagingData)
            }

            Then("page를 호출한다.") {
                verify(exactly = 1) { memoRepository.page(account.uid, tagId.value) }
            }
        }

        clearMocks(getAccountUseCase, memoRepository)
    }
})