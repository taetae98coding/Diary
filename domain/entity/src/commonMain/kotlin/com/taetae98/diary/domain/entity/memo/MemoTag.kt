package com.taetae98.diary.domain.entity.memo

public data class MemoTag(
    val memoId: String,
    val tagId: String,
) {
    public fun isInvalid(): Boolean {
        return memoId.isEmpty() || tagId.isEmpty()
    }
}
