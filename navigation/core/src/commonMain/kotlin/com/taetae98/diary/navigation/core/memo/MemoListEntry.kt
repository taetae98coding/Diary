package com.taetae98.diary.navigation.core.memo

import com.arkivanov.decompose.ComponentContext

public class MemoListEntry internal constructor(
    context: ComponentContext,
    public val navigateToTagDialog: () -> Unit,
    public val navigateToMemoAdd: () -> Unit,
    public val navigateToMemoDetail: (memoId: String) -> Unit,
) : ComponentContext by context
