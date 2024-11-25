package io.github.taetae98coding.diary.domain.tag.usecase

import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.domain.tag.repository.TagRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
public class PageTagMemoUseCase internal constructor(
	private val repository: TagRepository,
) {
	public operator fun invoke(tagId: String?): Flow<Result<List<Memo>>> {
		if (tagId.isNullOrBlank()) return flowOf(Result.success(emptyList()))

		return flow { emitAll(repository.findMemoByTagId(tagId)) }
			.mapLatest { Result.success(it) }
			.catch { emit(Result.failure(it)) }
	}
}
