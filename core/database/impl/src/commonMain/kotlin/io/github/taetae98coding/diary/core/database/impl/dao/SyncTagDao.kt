package io.github.taetae98coding.diary.core.database.impl.dao

import androidx.room3.Dao
import androidx.room3.Query
import io.github.taetae98coding.diary.core.database.api.entity.SyncStateLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.TagLocalEntity
import io.github.taetae98coding.diary.library.room.common.dao.RoomDao
import kotlin.uuid.Uuid

@Dao
internal interface SyncTagDao : RoomDao<SyncTagLocalEntity> {
    @Query(
        """
        SELECT Tag.*
        FROM Tag
        INNER JOIN SyncTag ON SyncTag.tagId = Tag.id
        INNER JOIN AccountTag ON AccountTag.tagId = Tag.id AND AccountTag.accountId = :accountId
        WHERE SyncTag.state = :state
        LIMIT 100
        """,
    )
    suspend fun getBySyncState(
        accountId: Uuid,
        state: SyncStateLocalEntity,
    ): List<TagLocalEntity>

    @Query(
        """
        SELECT COALESCE(MAX(Tag.updatedAt), 0)
        FROM Tag
        INNER JOIN SyncTag ON SyncTag.tagId = Tag.id
        INNER JOIN AccountTag ON AccountTag.tagId = Tag.id AND AccountTag.accountId = :accountId
        WHERE SyncTag.state = :state
        """,
    )
    suspend fun getLastUpdatedAt(
        accountId: Uuid,
        state: SyncStateLocalEntity,
    ): Long
}
