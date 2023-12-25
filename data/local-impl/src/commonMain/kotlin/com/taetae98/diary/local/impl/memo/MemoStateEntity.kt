package com.taetae98.diary.local.impl.memo

public enum class MemoStateEntity(
    internal val value: Long,
) {
    NONE(0L),
    FINISH(1L),
    DELETE(2L);
}
