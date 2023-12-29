package com.taetae98.diary.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal actual fun CoroutinesModule.getIoDispatcher(): CoroutineDispatcher {
    return Dispatchers.Default
}