package io.github.taetae98coding.diary.domain.calendar.usecase

import io.github.taetae98coding.diary.domain.calendar.entity.CalendarTagFilter
import io.github.taetae98coding.diary.domain.tag.usecase.PageTagUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
public class FindCalendarTagFilterUseCase internal constructor(
	private val pageTagUseCase: PageTagUseCase,
	private val findCalendarSelectedTagFilterUseCase: FindCalendarSelectedTagFilterUseCase,
) {
	public operator fun invoke(): Flow<Result<List<CalendarTagFilter>>> =
		flow {
			combine(
				pageTagUseCase().mapLatest { it.getOrThrow() },
				findCalendarSelectedTagFilterUseCase().mapLatest { it.getOrThrow() },
			) { pageTag, selectedTagFilter ->
				val selectedTagFilterIds = selectedTagFilter.map { it.id }.toSet()

				buildList {
					addAll(pageTag)
					addAll(selectedTagFilter)
				}.distinctBy {
					it.id
				}.sortedBy {
					it.detail.title
				}.map {
					CalendarTagFilter(
						tag = it,
						isSelected = selectedTagFilterIds.contains(it.id),
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
