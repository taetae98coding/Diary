package io.github.taetae98coding.diary.core.diary.database

import io.github.taetae98coding.diary.core.model.memo.MemoDto
import kotlinx.coroutines.flow.Flow

public interface MemoBackupDao {
    public suspend fun upsert(uid: String, memoId: String)
    public suspend fun deleteByMemoIds(memoIds: List<String>)

    public fun countByUid(uid: String): Flow<Int>
    public fun findByUid(uid: String): Flow<List<MemoDto>>
}
