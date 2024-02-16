package com.taetae98.diary.navigation.core.more

import com.arkivanov.decompose.ComponentContext

public class MoreEntry internal constructor(
    context: ComponentContext,
    public val navigateToAccount: () -> Unit,
    public val navigateToFinishedMemo: () -> Unit,
) : ComponentContext by context