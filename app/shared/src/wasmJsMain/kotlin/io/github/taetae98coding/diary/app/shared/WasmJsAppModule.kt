package io.github.taetae98coding.diary.app.shared

import io.github.taetae98coding.diary.feature.login.di.GoogleClientId
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
@Configuration
public class WasmJsAppModule {
    @Factory
    @GoogleClientId
    internal fun providesGoogleClientId(): String {
        return BuildKonfig.GOOGLE_CLIENT_ID
    }
}
