package io.github.taetae98coding.diary.compose.core.coroutine

import androidx.compose.runtime.Composable
import androidx.compose.runtime.retain.RetainObserver
import androidx.compose.runtime.retain.retain
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

private class RetainCoroutineScope(val scope: CoroutineScope) : RetainObserver {
    override fun onEnteredComposition() = Unit

    override fun onExitedComposition() = Unit

    override fun onRetained() = Unit

    override fun onRetired() {
        scope.cancel()
    }

    override fun onUnused() {
        scope.cancel()
    }
}

@Composable
public fun retainCoroutineScope(context: CoroutineContext = SupervisorJob() + Dispatchers.Main.immediate): CoroutineScope {
    return retain { RetainCoroutineScope(CoroutineScope(context)) }.scope
}
