package io.github.taetae98coding.diary.domain.calendar.usecase

import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.calendar.entity.CalendarTagFilter
import io.github.taetae98coding.diary.domain.calendar.repository.CalendarRepository
import io.github.taetae98coding.diary.domain.tag.repository.TagRepository
import io.github.taetae98coding.diary.domain.tag.usecase.PageTagUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
public class FindCalendarFilterTagUseCase internal constructor(private val getAccountUseCase: GetAccountUseCase, private val pageTagUseCase: PageTagUseCase, private val calendarRepository: CalendarRepository, private val tagRepository: TagRepository) {
	public operator fun invoke(): Flow<Result<List<CalendarTagFilter>>> =
		flow {
			val accountFlow = getAccountUseCase().mapLatest { it.getOrThrow() }
			val pageTagFlow = pageTagUseCase().mapLatest { it.getOrThrow() }
			val filterTagFlow =
				accountFlow
					.flatMapLatest { calendarRepository.findFilter(it.uid) }
					.flatMapLatest { tagRepository.findByIds(it) }

			combine(
				pageTagFlow,
				filterTagFlow,
			) { pageTag, filterTag ->
				val filterTagIds = filterTag.map { it.id }.toSet()

				buildList {
					addAll(pageTag)
					addAll(filterTag)
				}.distinctBy {
					it.id
				}.sortedBy {
					it.detail.title
				}.map {
					CalendarTagFilter(
						tag = it,
						isSelected = filterTagIds.contains(it.id),
					)
				}
			}.also {
				emitAll(it)
			}
		}.mapLatest {
			Result.success(it)
		}.catch {
			emit(Result.failure(it))
		}
}
