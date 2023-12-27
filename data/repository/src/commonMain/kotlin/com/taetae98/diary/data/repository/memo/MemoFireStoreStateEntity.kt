package com.taetae98.diary.data.repository.memo

public enum class MemoFireStoreStateEntity(
    internal val value: Int,
) {
    INCOMPLETE(0),
    COMPLETE(1),
    DELETE(2);
}
