package com.taetae98.diary.navigation.core.calendar

import com.arkivanov.decompose.ComponentContext

public class CalendarEntry(
    context: ComponentContext,
    public val navigateToMemoAdd: (dateRange: ClosedRange<Long>) -> Unit,
) : ComponentContext by context
