package com.taetae98.diary.navigation.core.memo

import com.arkivanov.decompose.ComponentContext

public class MemoListEntry(
    context: ComponentContext,
    public val navigateToMemoAdd: () -> Unit,
    public val navigateToMemoDetail: (memoId: String) -> Unit,
) : ComponentContext by context
