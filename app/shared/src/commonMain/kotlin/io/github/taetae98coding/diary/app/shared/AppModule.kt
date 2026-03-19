package io.github.taetae98coding.diary.app.shared

import io.github.taetae98coding.diary.core.supabase.impl.SupabaseConfig
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
@Configuration
public class AppModule {
    @Factory
    internal fun providesSupabaseConfig(): SupabaseConfig {
        return SupabaseConfig(
            url = BuildKonfig.SUPABASE_URL,
            key = BuildKonfig.SUPABASE_KEY,
        )
    }
}
