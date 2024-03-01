package com.taetae98.diary.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
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

    @Named(PROCESS)
    @Singleton
    internal fun providesProcessCoroutineScope(): CoroutineScope {
        return getProcessScope()
    }

    @Named(DATABASE)
    @Singleton
    internal fun providesDatabaseDispatcher(
        @Named(IO)
        coroutineDispatcher: CoroutineDispatcher
    ): CoroutineDispatcher {
        return coroutineDispatcher
    }

    public companion object {
        public const val MAIN: String = "main"
        public const val MAIN_IMMEDIATE: String = "mainImmediate"
        public const val IO: String = "io"
        public const val PROCESS: String = "process"

        public const val DATABASE: String = "databaseDispatcher"
    }
}
