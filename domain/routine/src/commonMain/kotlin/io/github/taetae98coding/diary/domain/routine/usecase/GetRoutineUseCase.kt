package io.github.taetae98coding.diary.domain.routine.usecase

import io.github.taetae98coding.diary.core.model.routine.Routine
import io.github.taetae98coding.diary.domain.routine.repository.RoutineRepository
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class GetRoutineUseCase(
    @param:Provided
    private val routineRepository: RoutineRepository,
) {
    public operator fun invoke(routineId: Uuid): Flow<Result<Routine?>> {
        return flow {
            routineRepository.get(routineId)
                .also { emitAll(it) }
        }.map {
            Result.success(it)
        }.catch {
            emit(Result.failure(it))
        }
    }
}
