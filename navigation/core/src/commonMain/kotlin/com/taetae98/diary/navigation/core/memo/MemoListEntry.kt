package com.taetae98.diary.navigation.core.memo

import com.arkivanov.decompose.ComponentContext

public class MemoListEntry(
    context: ComponentContext,
    public val navigateToMemoAdd: () -> Unit,
) : ComponentContext by context
