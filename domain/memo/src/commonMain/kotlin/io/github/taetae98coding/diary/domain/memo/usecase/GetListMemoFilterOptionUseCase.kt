package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.core.model.memo.ListMemoFilterOption
import io.github.taetae98coding.diary.domain.memo.repository.ListMemoFilterOptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class GetListMemoFilterOptionUseCase(
    @param:Provided
    private val listMemoFilterOptionRepository: ListMemoFilterOptionRepository,
) {
    public operator fun invoke(): Flow<Result<ListMemoFilterOption>> {
        return flow {
            listMemoFilterOptionRepository.get()
                .also { emitAll(it) }
        }.map {
            Result.success(it)
        }.catch {
            emit(Result.failure(it))
        }
    }
}
