package com.taetae98.diary.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Singleton

@Module
public class CoroutinesModule {
    @Named(MAIN)
    @Singleton
    internal fun providesMainDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }

    @Named(MAIN_IMMEDIATE)
    @Singleton
    internal fun providesMainImmediateDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main.immediate
    }

    @Named(IO)
    @Singleton
    internal fun providesIoDispatcher(): CoroutineDispatcher {
        return getIoDispatcher()
    }

    public companion object {
        public const val MAIN: String = "main"
        public const val MAIN_IMMEDIATE: String = "mainImmediate"
        public const val IO: String = "io"
    }
}