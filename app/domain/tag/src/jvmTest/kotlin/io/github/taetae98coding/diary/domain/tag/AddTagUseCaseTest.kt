package io.github.taetae98coding.diary.domain.tag

import io.github.taetae98coding.diary.common.exception.tag.TagTitleBlankException
import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.core.model.tag.TagDetail
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.backup.usecase.PushTagBackupQueueUseCase
import io.github.taetae98coding.diary.domain.tag.repository.TagRepository
import io.github.taetae98coding.diary.domain.tag.usecase.AddTagUseCase
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

class AddTagUseCaseTest : BehaviorSpec() {
	private val getAccountUseCase = mockk<GetAccountUseCase>(relaxed = true, relaxUnitFun = true)
	private val pushTagBackupQueueUseCase = mockk<PushTagBackupQueueUseCase>(relaxed = true, relaxUnitFun = true)
	private val clock = mockk<Clock> {
		every { now() } returns Instant.DISTANT_PAST
	}
	private val tagRepository = mockk<TagRepository>(relaxed = true, relaxUnitFun = true)
	private val useCase = AddTagUseCase(
		getAccountUseCase = getAccountUseCase,
		pushTagBackupQueueUseCase = pushTagBackupQueueUseCase,
		clock = clock,
		repository = tagRepository,
	)

	init {
		Given("detail title is not blank") {
			val detail = mockk<TagDetail> { every { title } returns "title" }

			When("call useCase") {
				val accountUid = "uid"
				val account = mockk<Account> { every { uid } returns accountUid }
				val tagId = "id"

				every { getAccountUseCase() } returns flowOf(Result.success(account))
				coEvery { tagRepository.getNextTagId() } returns tagId

				val result = useCase(detail)
				val tag = Tag(
					id = tagId,
					detail = detail,
					isFinish = false,
					isDelete = false,
					updateAt = Instant.DISTANT_PAST,
				)

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("upsert and backup") {
					coVerifyOrder {
						tagRepository.upsert(accountUid, tag)
						pushTagBackupQueueUseCase(tagId)
					}
				}

				clearAllMocks()
			}
		}

		Given("detail title is blank") {
			val detail = mockk<TagDetail> { every { title } returns "" }

			When("call useCase") {
				val result = useCase(detail)

				Then("result throw TagTitleBlankException") {
					result.shouldBeFailure<TagTitleBlankException>()
				}

				Then("do nothing") {
					verify {
						tagRepository wasNot Called
						clock wasNot Called
						pushTagBackupQueueUseCase wasNot Called
					}
				}

				clearAllMocks()
			}
		}
	}
}
