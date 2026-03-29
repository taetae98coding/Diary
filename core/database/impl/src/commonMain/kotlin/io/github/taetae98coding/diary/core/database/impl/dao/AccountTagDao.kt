package io.github.taetae98coding.diary.core.database.impl.dao

import androidx.paging.PagingSource
import androidx.room3.Dao
import androidx.room3.DaoReturnTypeConverters
import androidx.room3.Query
import androidx.room3.paging.PagingSourceDaoReturnTypeConverter
import io.github.taetae98coding.diary.core.database.api.entity.AccountTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.TagLocalEntity
import io.github.taetae98coding.diary.library.room.common.dao.RoomDao
import kotlin.uuid.Uuid

@Dao
@DaoReturnTypeConverters(PagingSourceDaoReturnTypeConverter::class)
internal interface AccountTagDao : RoomDao<AccountTagLocalEntity> {
    @Query(
        """
        SELECT Tag.*
        FROM Tag
        INNER JOIN AccountTag ON AccountTag.tagId = Tag.id AND AccountTag.accountId = :accountId
        WHERE (Tag.isFinished = 0 AND Tag.isDeleted = 0)
        ORDER BY Tag.title
        """,
    )
    fun page(accountId: Uuid): PagingSource<Int, TagLocalEntity>
}
