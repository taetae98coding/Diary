package com.taetae98.diary.navigation.core.tag

import com.arkivanov.decompose.ComponentContext
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

public class TagMemoEntry(
    context: ComponentContext,
    public val navigateUp: () -> Unit,
    public val navigateToTagDetail: (String) -> Unit,
    tagId: String,
) : ComponentContext by context {
    public val savedState: ImmutableMap<String, JsonElement> = persistentMapOf(
        TAG_ID to JsonPrimitive(tagId),
    )

    public companion object {
        public const val TAG_ID: String = "tagId"
    }
}
