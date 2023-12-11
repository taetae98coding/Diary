package com.taetae98.diary.navigation.core.account

import com.arkivanov.decompose.ComponentContext

public class AccountEntry(
    context: ComponentContext,
    public val navigateUp: () -> Unit,
) : ComponentContext by context