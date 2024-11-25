package io.github.taetae98coding.diary.domain.calendar.usecase

import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.calendar.repository.CalendarRepository
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import io.github.taetae98coding.diary.domain.tag.repository.TagRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.LocalDate
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
public class FindCalendarMemoUseCase internal constructor(private val getAccountUseCase: GetAccountUseCase, private val calendarRepository: CalendarRepository, private val memoRepository: MemoRepository, private val tagRepository: TagRepository) {
	public operator fun invoke(dateRange: ClosedRange<LocalDate>): Flow<Result<List<Memo>>> =
		flow {
			val accountFlow = getAccountUseCase().mapLatest { it.getOrThrow() }
			val calendarTagFilterFlow =
				accountFlow
					.flatMapLatest { calendarRepository.findFilter(it.uid) }
					.flatMapLatest { tagRepository.findByIds(it) }
					.mapLatest { list -> list.map { it.id }.toSet() }

			combine(accountFlow, calendarTagFilterFlow) { account, tagFilter ->
				memoRepository
					.findByDateRange(account.uid, dateRange, tagFilter)
					.flatMapLatest { memoList ->
						val tagIdSet = memoList.mapNotNull { it.primaryTag }.toSet()

						tagRepository.findByIds(tagIdSet).mapLatest { tagList ->
							val tagMap = tagList.associateBy { it.id }

							memoList.map { memo ->
								val color =
									tagMap[memo.primaryTag]
										?.detail
										?.color
										?: memo.detail.color

								memo.copy(detail = memo.detail.copy(color = color))
							}
						}
					}
			}.flatMapLatest {
				it
			}.also {
				emitAll(it)
			}
		}.mapLatest {
			Result.success(it)
		}.catch {
			it.printStackTrace()
			emit(Result.failure(it))
		}
}
