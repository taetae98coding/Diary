package io.github.taetae98coding.diary.domain.buddy.usecase

import io.github.taetae98coding.diary.common.exception.buddy.BuddyGroupTitleBlankException
import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.core.model.buddy.BuddyGroup
import io.github.taetae98coding.diary.core.model.buddy.BuddyGroupDetail
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.buddy.repository.BuddyRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class AddBuddyGroupUseCase internal constructor(
	private val getAccountUseCase: GetAccountUseCase,
	private val repository: BuddyRepository,
) {
	public suspend operator fun invoke(
		detail: BuddyGroupDetail,
		buddyIds: Set<String>,
	): Result<Unit> = runCatching {
		if (detail.title.isBlank()) {
			throw BuddyGroupTitleBlankException()
		}

		val account = getAccountUseCase().first().getOrThrow()
		if (account !is Account.Member) {
			throw IllegalStateException("Guest cannot add buddy group")
		}

		val buddyGroup = BuddyGroup(
			id = repository.getNextBuddyGroupId(),
			detail = detail,
		)

		val validBuddyIds = buildSet {
			addAll(buddyIds)
			add(account.uid)
		}

		repository.upsert(buddyGroup, validBuddyIds)
	}
}
