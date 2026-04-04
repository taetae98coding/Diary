package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.domain.memo.repository.MemoTagRepository
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class GetMemoTagUseCase(
    @param:Provided
    private val memoTagRepository: MemoTagRepository,
) {
    public operator fun invoke(memoId: Uuid): Flow<Result<List<Tag>>> {
        return flow { emitAll(memoTagRepository.getMemoTag(memoId)) }
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
    }
}
