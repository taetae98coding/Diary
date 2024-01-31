package com.taetae98.diary.core.coroutines

import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope

internal actual fun CoroutinesModule.getProcessScope(): CoroutineScope {
    return ProcessLifecycleOwner.get().lifecycleScope
}