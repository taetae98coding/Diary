package com.taetae98.diary.domain.entity.tag

import kotlin.jvm.JvmInline

@JvmInline
public value class TagId(public val value: String) {
    public fun isInvalid(): Boolean {
        return value.isEmpty()
    }
}
