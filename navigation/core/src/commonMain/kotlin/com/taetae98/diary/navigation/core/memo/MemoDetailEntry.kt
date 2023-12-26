package com.taetae98.diary.navigation.core.memo

import com.arkivanov.decompose.ComponentContext
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

public class MemoDetailEntry(
    context: ComponentContext,
    public val navigateUp: () -> Unit,
    memoId: String
) : ComponentContext by context {
    public val savedState: Map<String, JsonElement> = mapOf(
        ID to JsonPrimitive(memoId),
    )

    public companion object {
        public const val ID: String = "id"
    }
}
