package io.github.taetae98coding.diary.domain.memo

import io.github.taetae98coding.diary.common.exception.memo.MemoTitleBlankException
import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.backup.usecase.PushMemoBackupQueueUseCase
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import io.github.taetae98coding.diary.domain.memo.usecase.AddMemoUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class AddMemoUseCaseTest : BehaviorSpec() {
	private val getAccountUseCase = mockk<GetAccountUseCase>(relaxed = true, relaxUnitFun = true)
	private val pushMemoBackupQueueUseCase = mockk<PushMemoBackupQueueUseCase>(relaxed = true, relaxUnitFun = true)
	private val clock = mockk<Clock> { every { now() } returns Instant.DISTANT_PAST }
	private val memoRepository = mockk<MemoRepository>(relaxed = true, relaxUnitFun = true)
	private val useCase = AddMemoUseCase(
		getAccountUseCase = getAccountUseCase,
		pushMemoBackupQueueUseCase = pushMemoBackupQueueUseCase,
		clock = clock,
		repository = memoRepository,
	)

	init {
		Given("Title is blank") {
			val detail = mockk<MemoDetail> { every { title } returns "" }

			When("call useCase") {
				val result = useCase(detail, null, emptySet())

				Then("result throw MemoTitleBlankException") {
					result.shouldBeFailure<MemoTitleBlankException>()
				}

				Then("do nothing") {
					verify {
						getAccountUseCase wasNot Called
						memoRepository wasNot Called
						pushMemoBackupQueueUseCase wasNot Called
					}
				}

				clearAllMocks(answers = false)
			}
		}

		Given("Title is not blank") {
			val detail = mockk<MemoDetail> { every { title } returns "Title" }

			When("call useCase") {
				val accountUid = "uid"
				val account = mockk<Account.Member> { every { uid } returns accountUid }
				val memoId = "id"
				val primaryTag = "primaryTag"
				val tagIds = setOf("tagId")

				coEvery { memoRepository.getNextMemoId() } returns memoId
				every { getAccountUseCase() } returns flowOf(Result.success(account))

				val result = useCase(detail, primaryTag, tagIds)
				val memo = Memo(
					id = memoId,
					detail = detail,
					primaryTag = primaryTag,
					isFinish = false,
					isDelete = false,
					updateAt = Instant.DISTANT_PAST,
				)

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("upsert memo and backup") {
					coVerifyOrder {
						memoRepository.upsert(accountUid, memo, tagIds)
						pushMemoBackupQueueUseCase(memoId)
					}
				}

				clearAllMocks()
			}
		}
	}
}
