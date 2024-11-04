package io.github.taetae98coding.diary.core.diary.database.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Transaction
import androidx.room.Upsert

@Dao
internal abstract class EntityDao<T> {
    @Upsert
    abstract suspend fun upsert(entity: T)

    @Transaction
    @Upsert
    abstract suspend fun upsert(entityList: List<T>)

    @Delete
    abstract suspend fun delete(entity: T)
}
