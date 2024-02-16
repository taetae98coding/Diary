package com.taetae98.diary.navigation.core.calendar

import com.arkivanov.decompose.ComponentContext

public class CalendarEntry internal constructor(
    context: ComponentContext,
    public val navigateToMemoAdd: (dateRange: ClosedRange<Long>) -> Unit,
    public val navigateToMemoDetail: (memoId: String) -> Unit,
) : ComponentContext by context
