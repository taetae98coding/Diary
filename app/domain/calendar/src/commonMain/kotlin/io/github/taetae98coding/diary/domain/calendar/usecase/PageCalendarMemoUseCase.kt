package io.github.taetae98coding.diary.domain.calendar.usecase

import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
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
public class PageCalendarMemoUseCase internal constructor(
	private val getAccountUseCase: GetAccountUseCase,
	private val findCalendarSelectedTagFilterUseCase: FindCalendarSelectedTagFilterUseCase,
	private val memoRepository: MemoRepository,
	private val tagRepository: TagRepository,
) {
	public operator fun invoke(dateRange: ClosedRange<LocalDate>): Flow<Result<List<Memo>>> =
		flow {
			combine(
				getAccountUseCase().mapLatest { it.getOrThrow() },
				findCalendarSelectedTagFilterUseCase().mapLatest { it.getOrThrow() },
			) { account, selectedTagFilter ->
				account to selectedTagFilter.map { it.id }.toSet()
			}.flatMapLatest { (account, selectedTagFilterIds) ->
				memoRepository
					.findByDateRange(account.uid, dateRange, selectedTagFilterIds)
					.flatMapLatest { memoList ->
						val tagIds = memoList.mapNotNull { it.primaryTag }.toSet()
						val tag = tagRepository
							.getByIds(tagIds)
							.mapLatest { list -> list.filterNot { it.isDelete } }

						tag.mapLatest { tagList ->
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
			}.also {
				emitAll(it)
			}
		}.mapLatest {
			Result.success(it)
		}.catch {
			emit(Result.failure(it))
		}
}
