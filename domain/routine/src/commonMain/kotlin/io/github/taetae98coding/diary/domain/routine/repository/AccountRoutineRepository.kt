package io.github.taetae98coding.diary.domain.routine.repository

import androidx.paging.PagingData
import io.github.taetae98coding.diary.core.model.routine.Routine
import io.github.taetae98coding.diary.core.model.routine.RoutineDetail
import io.github.taetae98coding.diary.core.model.routine.RoutineRRule
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

public interface AccountRoutineRepository {
    public suspend fun add(
        accountId: Uuid,
        detail: RoutineDetail,
        rRules: List<RoutineRRule>,
        rDates: Set<LocalDate> = emptySet(),
        exDates: Set<LocalDate> = emptySet(),
    )

    public suspend fun update(
        routineId: Uuid,
        detail: RoutineDetail,
        rRules: List<RoutineRRule>,
        rDates: Set<LocalDate>,
        exDates: Set<LocalDate>,
    )

    public fun page(accountId: Uuid): Flow<PagingData<Routine>>
}
