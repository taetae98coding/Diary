package com.taetae98.diary.data.local.impl.memo

public enum class MemoStateEntity(
    internal val value: Long,
) {
    NONE(0L),
    COMPLETE(1L),
    DELETE(2L);
}
