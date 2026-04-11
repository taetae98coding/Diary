package io.github.taetae98coding.diary.library.koin.compose

import androidx.compose.runtime.retain.RetainObserver
import org.koin.core.scope.Scope

public class RetainKoinScope(public val scope: Scope) : RetainObserver {
    override fun onEnteredComposition(): Unit = Unit
    override fun onExitedComposition(): Unit = Unit
    override fun onRetained(): Unit = Unit
    override fun onRetired() {
        scope.close()
    }

    override fun onUnused() {
        scope.close()
    }
}
