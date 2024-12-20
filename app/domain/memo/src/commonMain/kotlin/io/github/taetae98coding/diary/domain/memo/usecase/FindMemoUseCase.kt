package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
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
public class FindMemoUseCase internal constructor(
	private val memoRepository: MemoRepository,
) {
	public operator fun invoke(memoId: String?): Flow<Result<Memo?>> {
		if (memoId.isNullOrBlank()) return flowOf(Result.success(null))

		return flow {
			memoRepository
				.getById(memoId)
				.mapLatest { memo -> memo?.takeUnless { it.isDelete } }
				.also { emitAll(it) }
		}.mapLatest {
			Result.success(it)
		}.catch { emit(Result.failure(it)) }
	}
}
