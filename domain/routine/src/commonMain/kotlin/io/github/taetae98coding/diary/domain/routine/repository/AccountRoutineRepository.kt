package io.github.taetae98coding.diary.domain.routine.repository

import androidx.paging.PagingData
import io.github.taetae98coding.diary.core.model.routine.Routine
import io.github.taetae98coding.diary.core.model.routine.RoutineDetail
import io.github.taetae98coding.diary.core.model.routine.RoutineRRule
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public interface AccountRoutineRepository {
    public suspend fun add(
        accountId: Uuid,
        detail: RoutineDetail,
        rRules: List<RoutineRRule>,
    )

    public suspend fun updateDetail(
        routineId: Uuid,
        detail: RoutineDetail,
    )

    public suspend fun updateFinish(
        routineId: Uuid,
        isFinished: Boolean,
    )

    public suspend fun updateDelete(
        routineId: Uuid,
        isDeleted: Boolean,
    )

    public fun getByYear(
        accountId: Uuid,
        year: Int,
    ): Flow<List<Routine>>

    public fun page(accountId: Uuid): Flow<PagingData<Routine>>
}
