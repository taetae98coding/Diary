package com.taetae98.diary.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

internal actual fun CoroutinesModule.getIoDispatcher(): CoroutineDispatcher {
    return Dispatchers.IO
}
