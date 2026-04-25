package io.github.taetae98coding.diary.domain.routine.usecase

import io.github.taetae98coding.diary.core.model.routine.RoutineDetail
import io.github.taetae98coding.diary.core.model.routine.RoutineRRule
import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.domain.routine.repository.AccountRoutineRepository
import io.github.taetae98coding.diary.domain.routine.repository.RoutineRepository
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.first
import kotlinx.datetime.LocalDate
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class UpdateRoutineUseCase(
    private val requestSyncUseCase: RequestSyncUseCase,
    @param:Provided
    private val routineRepository: RoutineRepository,
    @param:Provided
    private val accountRoutineRepository: AccountRoutineRepository,
) {
    public suspend operator fun invoke(
        routineId: Uuid,
        detail: RoutineDetail,
        rRules: List<RoutineRRule>,
        rDates: Set<LocalDate>,
        exDates: Set<LocalDate>,
    ): Result<Unit> {
        return runCatching {
            val routine = requireNotNull(routineRepository.get(routineId).first())

            accountRoutineRepository.update(
                routineId = routineId,
                detail = detail.copy(
                    title = detail.title.ifBlank { routine.detail.title },
                ),
                rRules = rRules.ifEmpty { routine.rRules },
                rDates = rDates,
                exDates = exDates,
            )

            requestSyncUseCase(SyncType.Background)
        }
    }
}
