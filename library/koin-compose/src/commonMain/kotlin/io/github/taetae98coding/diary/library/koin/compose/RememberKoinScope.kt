package io.github.taetae98coding.diary.library.koin.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.retain.retain
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.koin.compose.getKoin
import org.koin.core.scope.Scope
import org.koin.core.scope.ScopeCallback
import org.koin.core.scope.ScopeID

@Composable
public inline fun <reified T : Any> rememberCoroutineKoinScope(
    scopeId: ScopeID,
    coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Main.immediate,
): Scope {
    val koin = getKoin()
    val retainKoinScope = retain(koin) {
        val coroutineScope = CoroutineScope(coroutineContext)
        val scopeCallback = object : ScopeCallback {
            override fun onScopeClose(scope: Scope) {
                coroutineScope.cancel()
            }
        }
        val scope = koin.createScope<T>(scopeId = scopeId)
            .apply { declare(coroutineScope) }
            .apply { registerCallback(scopeCallback) }

        RetainKoinScope(scope)
    }

    return retainKoinScope.scope
}
