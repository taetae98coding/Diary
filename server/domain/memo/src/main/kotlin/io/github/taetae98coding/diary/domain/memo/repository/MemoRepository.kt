package io.github.taetae98coding.diary.domain.memo.repository

import io.github.taetae98coding.diary.core.model.Memo
import io.github.taetae98coding.diary.core.model.MemoAndTagIds
import io.github.taetae98coding.diary.core.model.MemoDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

public interface MemoRepository {
	public suspend fun upsert(list: List<MemoAndTagIds>, owner: String)
    public suspend fun update(id: String, detail: MemoDetail)
    public suspend fun updateFinish(id: String, isFinish: Boolean)
    public suspend fun updateDelete(id: String, isDelete: Boolean)

    public fun findById(id: String): Flow<Memo?>
    public fun findByIds(ids: Set<String>): Flow<List<Memo>>

	public fun findMemoAndTagIdsByUpdateAt(uid: String, updateAt: Instant): Flow<List<MemoAndTagIds>>
}
