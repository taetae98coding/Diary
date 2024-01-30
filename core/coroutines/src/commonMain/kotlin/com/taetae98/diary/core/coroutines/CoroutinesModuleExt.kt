package com.taetae98.diary.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

internal expect fun CoroutinesModule.getIoDispatcher(): CoroutineDispatcher

internal expect fun CoroutinesModule.getProcessScope(): CoroutineScope