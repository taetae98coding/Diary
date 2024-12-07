package io.github.taetae98coding.diary.domain.tag.repository

import io.github.taetae98coding.diary.core.model.memo.Memo
import kotlinx.coroutines.flow.Flow

public interface TagMemoRepository {
	public fun pageMemoByTagId(tagId: String): Flow<List<Memo>>
}
