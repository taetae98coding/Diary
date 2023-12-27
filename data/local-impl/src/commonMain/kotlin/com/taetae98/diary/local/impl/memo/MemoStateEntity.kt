package com.taetae98.diary.local.impl.memo

public enum class MemoStateEntity(
    internal val value: Long,
) {
    INCOMPLETE(0L),
    COMPLETE(1L),
    DELETE(2L);
}
