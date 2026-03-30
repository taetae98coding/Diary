package io.github.taetae98coding.diary.app.shared

import io.github.taetae98coding.diary.core.supabase.impl.SupabaseConfig
import io.github.taetae98coding.diary.core.weather.network.impl.di.WeatherApiKey
import kotlin.time.Clock
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan
@Configuration
public class AppModule {
    @Single
    internal fun providesClock(): Clock {
        return Clock.System
    }

    @Factory
    internal fun providesSupabaseConfig(): SupabaseConfig {
        return SupabaseConfig(
            url = BuildKonfig.SUPABASE_URL,
            key = BuildKonfig.SUPABASE_KEY,
        )
    }

    @WeatherApiKey
    @Factory
    internal fun providesWeatherApiKey(): String {
        return BuildKonfig.OPENWEATHERMAP_API_KEY
    }
}
