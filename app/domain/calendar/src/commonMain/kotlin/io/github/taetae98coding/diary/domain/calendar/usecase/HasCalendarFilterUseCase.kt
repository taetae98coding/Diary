package io.github.taetae98coding.diary.domain.calendar.usecase

import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.calendar.repository.CalendarRepository
import io.github.taetae98coding.diary.domain.tag.repository.TagRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
public class HasCalendarFilterUseCase internal constructor(private val getAccountUseCase: GetAccountUseCase, private val calendarRepository: CalendarRepository, private val tagRepository: TagRepository) {
	public operator fun invoke(): Flow<Result<Boolean>> =
		flow {
			getAccountUseCase()
				.mapLatest { it.getOrThrow() }
				.flatMapLatest { calendarRepository.findFilter(it.uid) }
				.flatMapLatest { tagRepository.findByIds(it) }
				.mapLatest { it.isNotEmpty() }
				.also { emitAll(it) }
		}.mapLatest {
			Result.success(it)
		}.catch {
			emit(Result.failure(it))
		}
}
