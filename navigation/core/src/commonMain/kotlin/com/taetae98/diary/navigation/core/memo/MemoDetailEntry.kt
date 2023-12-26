package com.taetae98.diary.navigation.core.memo

import com.arkivanov.decompose.ComponentContext

public class MemoDetailEntry(
    context: ComponentContext,
    public val navigateUp: () -> Unit,
    private val memoId: String
) : ComponentContext by context
