package com.taetae98.diary.domain.usecase.memo

import androidx.paging.PagingData
import com.taetae98.diary.domain.entity.account.Account
import com.taetae98.diary.domain.entity.tag.Tag
import com.taetae98.diary.domain.exception.NoAccountException
import com.taetae98.diary.domain.repository.MemoRepository
import com.taetae98.diary.domain.repository.SelectTagByMemoRepository
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.tag.select.FindTagInMemoUseCase
import com.taetae98.diary.domain.usecase.tag.select.GetHasToPageNoTagMemoUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeFailure
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class PageMemoBySelectTagUseCaseBddTest : BehaviorSpec({
    val getAccountUseCase = mockk<GetAccountUseCase>()
    val findTagInMemoUseCase = mockk<FindTagInMemoUseCase>()
    val getHasToPageNoTagMemoUseCase = mockk<GetHasToPageNoTagMemoUseCase>()
    val memoRepository = mockk<MemoRepository>()
    val selectTagByMemoRepository = mockk<SelectTagByMemoRepository>()
    val useCase = PageMemoBySelectTagUseCase(
        getAccountUseCase = getAccountUseCase,
        findTagInMemoUseCase = findTagInMemoUseCase,
        getHasToPageNoTagMemoUseCase = getHasToPageNoTagMemoUseCase,
        memoRepository = memoRepository,
        selectTagByMemoRepository = selectTagByMemoRepository,
    )

    Given("Account를 불러오지 못할 때") {
        every { getAccountUseCase(Unit) } returns flowOf(Result.failure(Exception()))

        When("UseCase를 호출하면") {
            val result = useCase(Unit).first()

            Then("실패한다.") {
                result.shouldBeFailure<NoAccountException>()
            }
        }

        clearMocks(getAccountUseCase)
    }


    Given("FindTagInMemoUseCase가 실패할 때") {
        every { getAccountUseCase(Unit) } returns flowOf(Result.success(Account.Guest))
        every { findTagInMemoUseCase(Unit) } returns flowOf(Result.failure(Exception()))
        every { getHasToPageNoTagMemoUseCase(Unit) } returns flowOf(Result.success(true))

        When("UseCase를 호출하면") {
            val result = useCase(Unit).first()

            Then("실패한다.") {
                result.shouldBeFailure()
            }
        }

        clearMocks(getAccountUseCase, findTagInMemoUseCase, getHasToPageNoTagMemoUseCase)
    }

    Given("GetHasToPageNoTagMemoUseCase 실패할 때") {
        every { getAccountUseCase(Unit) } returns flowOf(Result.success(Account.Guest))
        every { findTagInMemoUseCase(Unit) } returns flowOf(Result.success(emptyList()))
        every { getHasToPageNoTagMemoUseCase(Unit) } returns flowOf(Result.failure(Exception()))

        When("UseCase를 호출하면") {
            val result = useCase(Unit).first()

            Then("실패한다.") {
                result.shouldBeFailure()
            }
        }

        clearMocks(getAccountUseCase, findTagInMemoUseCase, getHasToPageNoTagMemoUseCase)
    }

    Given("모든 UseCase가 성공하고 선택된 태그가 없을 때") {
        val account = Account.Guest

        every { getAccountUseCase(Unit) } returns flowOf(Result.success(account))
        every { findTagInMemoUseCase(Unit) } returns flowOf(Result.success(emptyList()))
        every { getHasToPageNoTagMemoUseCase(Unit) } returns flowOf(Result.success(false))
        every { memoRepository.page(account.uid) } returns flowOf(PagingData.empty())

        When("UseCase를 호출하면") {
            useCase(Unit).first()

            Then("전체 메모를 페이징한다.") {
                verify(exactly = 1) { memoRepository.page(account.uid) }
            }
        }

        clearMocks(getAccountUseCase, findTagInMemoUseCase, getHasToPageNoTagMemoUseCase, memoRepository)
    }

    Given("모든 UseCase가 성공하고 선택된 태그가 있을 때") {
        val account = Account.Guest
        val tagList = listOf<Tag>(mockk(), mockk())

        every { getAccountUseCase(Unit) } returns flowOf(Result.success(account))
        every { findTagInMemoUseCase(Unit) } returns flowOf(Result.success(tagList))
        every { getHasToPageNoTagMemoUseCase(Unit) } returns flowOf(Result.success(false))
        every { selectTagByMemoRepository.page(account.uid, false) } returns flowOf(PagingData.empty())

        When("UseCase를 호출하면") {
            useCase(Unit).first()

            Then("태그가 있는 메모를 페이징한다.") {
                verify(exactly = 1) { selectTagByMemoRepository.page(account.uid, false) }
            }
        }

        clearMocks(getAccountUseCase, findTagInMemoUseCase, getHasToPageNoTagMemoUseCase, selectTagByMemoRepository)
    }
})