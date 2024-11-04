package io.github.taetae98coding.diary.domain.memo.repository

import io.github.taetae98coding.diary.core.model.Memo
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

public interface MemoRepository {
	public suspend fun upsert(list: List<Memo>)

	public fun findByIds(list: List<String>): Flow<List<Memo>>

	public fun findByUpdateAt(uid: String, updateAt: Instant): Flow<List<Memo>>
}
