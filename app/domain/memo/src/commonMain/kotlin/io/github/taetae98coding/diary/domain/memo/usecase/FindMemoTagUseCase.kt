package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.core.model.tag.TagWithPrimary
import io.github.taetae98coding.diary.domain.memo.repository.MemoTagRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
public class FindMemoTagUseCase internal constructor(
    private val findMemoUseCase: FindMemoUseCase,
    private val repository: MemoTagRepository,
) {
    public operator fun invoke(memoId: String?): Flow<Result<List<TagWithPrimary>>> {
        if (memoId.isNullOrBlank()) return flowOf(Result.success(emptyList()))

        return flow {
            val memoFlow = findMemoUseCase(memoId).mapLatest { it.getOrThrow() }
            val tagListFlow = repository.findByMemoId(memoId)

            combine(memoFlow, tagListFlow) { memo, tagList ->
                if (memo == null) return@combine emptyList()

                tagList.map {
                    TagWithPrimary(
                        tag = it,
                        isPrimary = memo.primaryTag == it.id
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
