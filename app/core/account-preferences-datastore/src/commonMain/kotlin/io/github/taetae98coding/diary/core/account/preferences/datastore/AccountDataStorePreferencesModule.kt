package io.github.taetae98coding.diary.core.account.preferences.datastore

import io.github.taetae98coding.diary.core.account.preferences.AccountPreferences
import io.github.taetae98coding.diary.library.koin.datastore.getDataStore
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton
import org.koin.core.component.KoinComponent

@Module
@ComponentScan
public class AccountDataStorePreferencesModule : KoinComponent {
    @Singleton
    internal fun providesAccountPreferences(): AccountPreferences {
        return AccountDataStorePreferences(getDataStore("account.preferences_pb"))
    }
}
