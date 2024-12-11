package io.github.taetae98coding.diary.core.diary.database

public interface MemoAccountDao {
	public fun hasPermission(owner: String, memoId: String): Boolean
}
