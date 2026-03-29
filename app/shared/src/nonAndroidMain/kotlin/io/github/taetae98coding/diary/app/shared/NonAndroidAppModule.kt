package io.github.taetae98coding.diary.app.shared

import io.github.taetae98coding.diary.data.sync.di.SyncCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
public class NonAndroidAppModule {
    @Single
    @SyncCoroutineScope
    internal fun providesSyncCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
}
