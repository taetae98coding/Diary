package com.taetae98.diary.navigation.core.memo

import com.arkivanov.decompose.ComponentContext

public class MemoListTagDialogEntry internal constructor(
    context: ComponentContext,
    public val onDismiss: () -> Unit,
) : ComponentContext by context