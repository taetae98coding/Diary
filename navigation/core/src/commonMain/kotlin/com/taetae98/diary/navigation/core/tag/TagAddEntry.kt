package com.taetae98.diary.navigation.core.tag

import com.arkivanov.decompose.ComponentContext

public class TagAddEntry(
    context: ComponentContext,
    public val navigateUp: () -> Unit
) : ComponentContext by context
