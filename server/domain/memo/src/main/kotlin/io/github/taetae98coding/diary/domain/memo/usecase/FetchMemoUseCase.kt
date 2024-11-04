package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.core.model.Memo
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.Instant
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
public class FetchMemoUseCase internal constructor(
	private val repository: MemoRepository,
) {
	public operator fun invoke(uid: String, updateAt: Instant): Flow<Result<List<Memo>>> =
		flow { emitAll(repository.findByUpdateAt(uid, updateAt)) }
			.mapLatest { Result.success(it) }
			.catch { emit(Result.failure(it)) }
}
