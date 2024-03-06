package com.taetae98.diary.navigation.core.tag

import com.arkivanov.decompose.ComponentContext
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

public class TagMemoEntry internal constructor(
    context: ComponentContext,
    public val navigateUp: () -> Unit,
    private val navigateToTagDetail: (String) -> Unit,
    private val navigateToMemoAdd: (tagIdSet: Set<String>) -> Unit,
    public val navigateToMemoDetail: (String) -> Unit,
    private val tagId: String,
) : ComponentContext by context {
    public val savedState: ImmutableMap<String, JsonElement> = persistentMapOf(
        TAG_ID to JsonPrimitive(tagId),
    )

    public fun onNavigateToTagDetail() {
        navigateToTagDetail(tagId)
    }

    public fun onNavigateToMemoAdd() {
        navigateToMemoAdd(setOf(tagId))
    }

    public companion object {
        public const val TAG_ID: String = "tagId"
    }
}
