package com.taetae98.diary.data.repository.memo

public enum class MemoFireStoreStateEntity(
    internal val value: Long,
) {
    INCOMPLETE(0L),
    COMPLETE(1L),
    DELETE(2L);

    public companion object {
        public fun valueOf(value: Long): MemoFireStoreStateEntity {
            return entries.find { it.value == value } ?: INCOMPLETE
        }
    }
}
