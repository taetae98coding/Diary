package com.taetae98.diary.navigation.core.more

import com.arkivanov.decompose.ComponentContext

public class MoreEntry(
    context: ComponentContext,
    public val navigateToAccount:() ->Unit
) : ComponentContext by context