package com.taetae98.diary.domain.entity.memo

import kotlin.jvm.JvmInline

@JvmInline
public value class MemoId(public val value: String) {
    public fun isInvalid(): Boolean {
        return value.isEmpty()
    }
}
