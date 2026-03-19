package io.github.taetae98coding.diary.core.database.impl.dao

import androidx.room3.Dao
import io.github.taetae98coding.diary.core.database.api.entity.AccountMemoLocalEntity
import io.github.taetae98coding.diary.library.room.common.dao.RoomDao

@Dao
internal interface AccountMemoDao : RoomDao<AccountMemoLocalEntity>
