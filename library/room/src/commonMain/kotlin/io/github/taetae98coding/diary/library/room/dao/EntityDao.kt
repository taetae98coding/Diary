package io.github.taetae98coding.diary.library.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Transaction
import androidx.room.Upsert

@Dao
public abstract class EntityDao<T> {
    @Upsert
    public abstract suspend fun upsert(entity: T)

    @Transaction
    @Upsert
    public abstract suspend fun upsert(entityList: List<T>)

    @Delete
    public abstract suspend fun delete(entity: T)
}
