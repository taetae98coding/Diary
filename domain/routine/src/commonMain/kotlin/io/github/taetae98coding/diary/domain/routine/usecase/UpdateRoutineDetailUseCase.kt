package io.github.taetae98coding.diary.domain.routine.usecase

import io.github.taetae98coding.diary.core.model.routine.RoutineDetail
import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.domain.routine.repository.AccountRoutineRepository
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class UpdateRoutineDetailUseCase(
    private val requestSyncUseCase: RequestSyncUseCase,
    @param:Provided
    private val accountRoutineRepository: AccountRoutineRepository,
) {
    public suspend operator fun invoke(
        routineId: Uuid,
        detail: RoutineDetail,
    ): Result<Unit> {
        return runCatching {
            accountRoutineRepository.updateDetail(
                routineId = routineId,
                detail = detail,
            )
            requestSyncUseCase(SyncType.Background)
        }
    }
}
