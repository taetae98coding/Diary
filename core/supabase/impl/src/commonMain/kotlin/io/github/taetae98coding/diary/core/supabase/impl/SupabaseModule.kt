package io.github.taetae98coding.diary.core.supabase.impl

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.functions.Functions
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Module
@ComponentScan
@Configuration
public class SupabaseModule {
    @Single
    internal fun providesSupabaseClient(
        @Provided
        config: SupabaseConfig,
    ): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = config.url,
            supabaseKey = config.key,
        ) {
            install(Auth)
            install(Functions)
        }
    }
}
