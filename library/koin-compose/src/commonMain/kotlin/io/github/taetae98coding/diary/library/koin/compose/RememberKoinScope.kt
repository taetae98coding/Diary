package io.github.taetae98coding.diary.library.koin.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.retain.RetainedEffect
import androidx.compose.runtime.retain.retain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.koin.compose.getKoin
import org.koin.core.scope.Scope
import org.koin.core.scope.ScopeCallback
import org.koin.core.scope.ScopeID

@Composable
public inline fun <reified T : Any> rememberKoinScope(
    scopeId: ScopeID,
    autoClose: Boolean = false,
): Scope {
    val koin = getKoin()
    val scope = retain(koin) {
        val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        val scopeCallback = object : ScopeCallback {
            override fun onScopeClose(scope: Scope) {
                coroutineScope.cancel()
            }
        }

        koin
            .getOrCreateScope<T>(scopeId)
            .apply { declare(coroutineScope) }
            .apply { registerCallback(scopeCallback) }
    }

    if (autoClose) {
        RetainedEffect(scope) {
            onRetire { scope.close() }
        }
    }

    return scope
}
