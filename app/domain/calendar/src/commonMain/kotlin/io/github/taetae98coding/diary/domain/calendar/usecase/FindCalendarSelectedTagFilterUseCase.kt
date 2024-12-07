package io.github.taetae98coding.diary.domain.calendar.usecase

import io.github.taetae98coding.diary.core.model.tag.Tag
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
public class FindCalendarSelectedTagFilterUseCase internal constructor(
	private val getAccountUseCase: GetAccountUseCase,
	private val calendarRepository: CalendarRepository,
	private val tagRepository: TagRepository,
) {
	public operator fun invoke(): Flow<Result<List<Tag>>> = flow {
		getAccountUseCase()
			.mapLatest { it.getOrThrow() }
			.flatMapLatest { calendarRepository.findFilter(it.uid) }
			.flatMapLatest { tagRepository.getByIds(it) }
			.mapLatest { list -> list.filterNot { it.isDelete } }
			.also { emitAll(it) }
	}.mapLatest {
		Result.success(it)
	}.catch {
		emit(Result.failure(it))
	}
}
