package com.taetae98.diary.navigation.core.tag

import com.arkivanov.decompose.ComponentContext

public class TagListEntry(
    context: ComponentContext,
    public val navigateToTagAdd: () -> Unit,
) : ComponentContext by context {
}