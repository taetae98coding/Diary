package com.taetae98.diary.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher

internal expect fun CoroutinesModule.getIoDispatcher(): CoroutineDispatcher