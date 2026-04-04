package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class GetMemoUseCase(
    @param:Provided
    private val memoRepository: MemoRepository,
) {
    public operator fun invoke(memoId: Uuid): Flow<Result<Memo?>> {
        return memoRepository.get(memoId)
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
    }
}
