package io.github.taetae98coding.diary.domain.routine.usecase

import androidx.paging.PagingData
import io.github.taetae98coding.diary.core.model.routine.Routine
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.routine.repository.AccountRoutineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class PageRoutineUseCase(
    private val getAccountUseCase: GetAccountUseCase,
    @param:Provided
    private val accountRoutineRepository: AccountRoutineRepository,
) {
    public operator fun invoke(): Flow<Result<PagingData<Routine>>> {
        return flow {
            getAccountUseCase().mapLatest { it.getOrThrow() }
                .flatMapLatest { accountRoutineRepository.page(it.accountId) }
                .also { emitAll(it) }
        }.map {
            Result.success(it)
        }.catch {
            emit(Result.failure(it))
        }
    }
}
