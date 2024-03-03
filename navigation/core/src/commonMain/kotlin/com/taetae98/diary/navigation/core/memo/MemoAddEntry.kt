package com.taetae98.diary.navigation.core.memo

import com.arkivanov.decompose.ComponentContext
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

public class MemoAddEntry internal constructor(
    context: ComponentContext,
    public val navigateUp: () -> Unit,
    dateRange: ClosedRange<Long>?,
) : ComponentContext by context {
    public val savedState: ImmutableMap<String, JsonElement> = buildMap {
        put(HAS_DATE, JsonPrimitive(dateRange != null))
        dateRange?.let { put(START, JsonPrimitive(it.start)) }
        dateRange?.let { put(END_INCLUSIVE, JsonPrimitive(it.endInclusive)) }
    }.toImmutableMap()

    public companion object {
        public const val HAS_DATE: String = "hasDate"
        public const val START: String = "start"
        public const val END_INCLUSIVE: String = "endInclusive"
    }
}