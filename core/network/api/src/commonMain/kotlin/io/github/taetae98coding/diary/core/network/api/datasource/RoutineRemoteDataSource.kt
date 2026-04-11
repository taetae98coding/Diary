package io.github.taetae98coding.diary.core.network.api.datasource

import io.github.taetae98coding.diary.core.network.api.entity.RoutineRemoteEntity

public interface RoutineRemoteDataSource {
    public suspend fun push(routineList: List<RoutineRemoteEntity>)

    public suspend fun pull(updatedAt: Long): List<RoutineRemoteEntity>
}
