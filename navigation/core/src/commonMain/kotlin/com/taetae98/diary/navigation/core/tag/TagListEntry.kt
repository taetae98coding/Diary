package com.taetae98.diary.navigation.core.tag

import com.arkivanov.decompose.ComponentContext

public class TagListEntry internal constructor(
    context: ComponentContext,
    public val navigateToTagAdd: () -> Unit,
    public val navigateToTagMemo: (String) -> Unit,
) : ComponentContext by context
