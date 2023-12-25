package com.taetae98.diary.data.repository.memo

public enum class MemoFireStoreStateEntity(
    internal val value: Int,
) {
    NONE(0),
    FINISH(1),
    DELETE(2);
}
