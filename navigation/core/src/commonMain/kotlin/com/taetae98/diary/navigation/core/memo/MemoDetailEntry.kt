package com.taetae98.diary.navigation.core.memo

import com.arkivanov.decompose.ComponentContext
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

public class MemoDetailEntry internal constructor(
    context: ComponentContext,
    public val navigateUp: () -> Unit,
    memoId: String
) : ComponentContext by context {
    public val savedState: ImmutableMap<String, JsonElement> = persistentMapOf(
        ID to JsonPrimitive(memoId),
    )

    public companion object {
        public const val ID: String = "id"
    }
}
