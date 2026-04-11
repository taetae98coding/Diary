package io.github.taetae98coding.diary.domain.routine.usecase

import io.github.taetae98coding.diary.core.model.routine.RoutineRRule
import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.domain.routine.repository.AccountRoutineRRuleRepository
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class RemoveRoutineRRuleUseCase(
    private val requestSyncUseCase: RequestSyncUseCase,
    @param:Provided
    private val accountRoutineRRuleRepository: AccountRoutineRRuleRepository,
) {
    public suspend operator fun invoke(
        routineId: Uuid,
        rRule: RoutineRRule,
    ): Result<Unit> {
        return runCatching {
            accountRoutineRRuleRepository.remove(routineId = routineId, rRule = rRule)
            requestSyncUseCase(SyncType.Background)
        }
    }
}
