package io.github.taetae98coding.diary.domain.calendar.usecase

import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.calendar.repository.CalendarRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class DeleteCalendarTagUseCase internal constructor(private val getAccountUseCase: GetAccountUseCase, private val repository: CalendarRepository) {
	public suspend operator fun invoke(tagId: String): Result<Unit> =
		runCatching {
			val account = getAccountUseCase().first().getOrThrow()
			repository.delete(account.uid, tagId)
		}
}
