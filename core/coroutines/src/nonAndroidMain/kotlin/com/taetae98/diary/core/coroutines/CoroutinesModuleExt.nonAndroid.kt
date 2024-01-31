package com.taetae98.diary.core.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

private val processScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

internal actual fun CoroutinesModule.getProcessScope(): CoroutineScope {
    return processScope
}
