package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.domain.memo.entity.MemoTag
import io.github.taetae98coding.diary.domain.memo.repository.MemoTagRepository
import io.github.taetae98coding.diary.domain.tag.repository.TagRepository
import io.github.taetae98coding.diary.domain.tag.usecase.PageTagUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
public class FindMemoTagUseCase internal constructor(private val findMemoUseCase: FindMemoUseCase, private val pageTagUseCase: PageTagUseCase, private val memoTagRepository: MemoTagRepository, private val tagRepository: TagRepository) {
	public operator fun invoke(memoId: String?): Flow<Result<List<MemoTag>>> {
		if (memoId.isNullOrBlank()) return flowOf(Result.success(emptyList()))

		return flow {
			val memoFlow = findMemoUseCase(memoId).mapLatest { it.getOrThrow() }
			val selectedTagFlow =
				memoTagRepository
					.findTagIdsByMemoId(memoId)
					.flatMapLatest { tagRepository.findByIds(it) }
			val pageTagFlow = pageTagUseCase().mapLatest { it.getOrThrow() }

			combine(memoFlow, selectedTagFlow, pageTagFlow) { memo, selectedTag, pageTag ->
				if (memo == null) return@combine emptyList()

				val selectedTagIds = selectedTag.map { it.id }.toSet()

				buildList {
					addAll(selectedTag)
					addAll(pageTag)
				}.distinctBy {
					it.id
				}.sortedBy {
					it.detail.title
				}.map {
					MemoTag(
						tag = it,
						isSelected = selectedTagIds.contains(it.id),
						isPrimary = memo.primaryTag == it.id,
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
}
