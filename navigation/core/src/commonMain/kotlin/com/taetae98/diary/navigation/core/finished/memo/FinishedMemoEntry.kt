package com.taetae98.diary.navigation.core.finished.memo

import com.arkivanov.decompose.ComponentContext

public class FinishedMemoEntry internal constructor(
    context: ComponentContext,
    public val navigateUp: () -> Unit,
) : ComponentContext by context
