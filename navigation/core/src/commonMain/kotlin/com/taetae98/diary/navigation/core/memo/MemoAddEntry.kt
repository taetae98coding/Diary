package com.taetae98.diary.navigation.core.memo

import com.arkivanov.decompose.ComponentContext

public class MemoAddEntry(
    context: ComponentContext,
    public val navigateUp: () -> Unit,
) : ComponentContext by context {
}