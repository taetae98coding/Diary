package io.github.taetae98coding.diary.core.coroutines

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

public data object AppLifecycleOwner : LifecycleOwner {
    private val registry = LifecycleRegistry(this)

    override val lifecycle: Lifecycle
        get() = registry

    public fun create() {
        registry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    public fun start() {
        registry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    public fun resume() {
        registry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    public fun pause() {
        registry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    }

    public fun stop() {
        registry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    public fun destroy() {
        registry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }
}
