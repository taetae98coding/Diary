package io.github.taetae98coding.diary.domain.routine.usecase

import io.github.taetae98coding.diary.core.model.routine.RoutineDetail
import io.github.taetae98coding.diary.core.model.routine.RoutineRRule
import io.github.taetae98coding.diary.core.model.routine.RoutineRRuleEmptyException
import io.github.taetae98coding.diary.core.model.routine.RoutineTitleBlankException
import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.routine.repository.AccountRoutineRepository
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class AddRoutineUseCase(
    private val getAccountUseCase: GetAccountUseCase,
    private val requestSyncUseCase: RequestSyncUseCase,
    @param:Provided
    private val accountRoutineRepository: AccountRoutineRepository,
) {
    public suspend operator fun invoke(
        detail: RoutineDetail,
        rRules: List<RoutineRRule>,
        isCalendarVisible: Boolean,
    ): Result<Unit> {
        return runCatching {
            if (detail.title.isBlank()) throw RoutineTitleBlankException()
            if (rRules.isEmpty()) throw RoutineRRuleEmptyException()

            val account = getAccountUseCase().first().getOrThrow()

            accountRoutineRepository.add(
                accountId = account.accountId,
                detail = detail,
                rRules = rRules,
                isCalendarVisible = isCalendarVisible,
            )

            requestSyncUseCase(SyncType.Background)
        }
    }
}
