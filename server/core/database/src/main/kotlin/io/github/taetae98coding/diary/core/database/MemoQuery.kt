package io.github.taetae98coding.diary.core.database

import io.github.taetae98coding.diary.core.model.Memo

public data object MemoQuery {
    public fun upsertMemo(memo: Memo, tagIds: Set<String>) {
        MemoTable.upsert(memo)
        MemoTagTable.deleteByMemoId(memo.id)
        tagIds.forEach {
            MemoTagTable.upsert(memo.id, it)
        }
    }
}
