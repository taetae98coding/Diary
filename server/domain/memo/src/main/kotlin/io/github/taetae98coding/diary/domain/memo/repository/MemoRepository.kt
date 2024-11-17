package io.github.taetae98coding.diary.domain.memo.repository

import io.github.taetae98coding.diary.core.model.Memo
import io.github.taetae98coding.diary.core.model.MemoAndTagIds
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

public interface MemoRepository {
	public suspend fun upsert(list: List<MemoAndTagIds>)

	public fun findByIds(ids: Set<String>): Flow<List<Memo>>

	public fun findMemoAndTagIdsByUpdateAt(uid: String, updateAt: Instant): Flow<List<MemoAndTagIds>>
}
