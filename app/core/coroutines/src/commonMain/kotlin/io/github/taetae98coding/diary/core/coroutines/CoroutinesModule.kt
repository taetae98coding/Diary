package io.github.taetae98coding.diary.core.coroutines

import androidx.lifecycle.LifecycleOwner
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
@ComponentScan
public class CoroutinesModule {
    @Singleton
    internal fun providesAppLifecycleOwner(): LifecycleOwner {
        return getAppLifecycleOwner()
    }
}
