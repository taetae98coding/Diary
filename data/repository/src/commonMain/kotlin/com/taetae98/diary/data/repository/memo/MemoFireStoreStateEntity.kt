package com.taetae98.diary.data.repository.memo

public enum class MemoFireStoreStateEntity(
    internal val value: String,
) {
    NONE("NONE"),
    FINISH("FINISH"),
    DELETE("DELETE");
}
