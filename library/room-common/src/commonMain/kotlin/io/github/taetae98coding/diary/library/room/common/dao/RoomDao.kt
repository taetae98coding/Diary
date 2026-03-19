package io.github.taetae98coding.diary.library.room.common.dao

import androidx.room3.Dao
import androidx.room3.Transaction
import androidx.room3.Upsert

@Dao
public interface RoomDao<E> {
    @Upsert
    public suspend fun upsert(entity: E)

    @Transaction
    @Upsert
    public suspend fun upsert(entities: Collection<E>)
}
