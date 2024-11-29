package io.github.taetae98coding.diary.core.diary.database

import io.github.taetae98coding.diary.core.model.memo.MemoDto
import kotlinx.coroutines.flow.Flow

public interface TagMemoDao {
	public fun pageMemoByTagId(tagId: String): Flow<List<MemoDto>>
}
