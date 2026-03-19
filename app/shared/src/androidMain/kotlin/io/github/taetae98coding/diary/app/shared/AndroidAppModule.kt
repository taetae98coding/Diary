package io.github.taetae98coding.diary.app.shared

import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import io.github.taetae98coding.diary.feature.login.di.GoogleClientId
import kotlinx.coroutines.CoroutineScope
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
public class AndroidAppModule {
    @Factory
    @GoogleClientId
    internal fun providesGoogleClientId(): String {
        return BuildKonfig.GOOGLE_CLIENT_ID
    }

    @Single
    internal fun providesSyncCoroutineScope(): CoroutineScope {
        return ProcessLifecycleOwner.get().lifecycleScope
    }
}
