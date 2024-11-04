package io.github.taetae98coding.diary.fore.account.preferences.memory

import io.github.taetae98coding.diary.core.account.preferences.AccountPreferences
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
@ComponentScan
public class AccountPreferencesMemoryModule {
    @Singleton
    internal fun providesAccountPreferences(): AccountPreferences {
        return AccountMemoryPreferences
    }
}
