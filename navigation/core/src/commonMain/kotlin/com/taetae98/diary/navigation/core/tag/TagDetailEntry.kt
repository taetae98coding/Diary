package com.taetae98.diary.navigation.core.tag

import com.arkivanov.decompose.ComponentContext
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

public class TagDetailEntry internal constructor(
    context: ComponentContext,
    public val navigateUp: () -> Unit,
    tagId: String,
) : ComponentContext by context {
    public val savedState: ImmutableMap<String, JsonElement> = persistentMapOf(
        ID to JsonPrimitive(tagId),
    )

    public companion object {
        public const val ID: String = "id"
    }
}
