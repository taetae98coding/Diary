package io.github.taetae98coding.diary.initializer

import io.github.taetae98coding.diary.app.AppModule
import io.github.taetae98coding.diary.core.account.preferences.datastore.AccountDataStorePreferencesModule
import io.github.taetae98coding.diary.core.diary.database.room.DiaryRoomDatabaseModule
import io.github.taetae98coding.diary.core.holiday.database.room.HolidayRoomDatabaseModule
import io.github.taetae98coding.diary.core.holiday.preferences.datastore.HolidayDataStorePreferencesModule
import org.koin.core.module.Module
import org.koin.ksp.generated.module

internal actual val appModule: Module
    get() = AppModule().module
internal actual val accountDataStoreModule: Module
    get() = AccountDataStorePreferencesModule().module
internal actual val holidayDataStoreModule: Module
    get() = HolidayDataStorePreferencesModule().module
internal actual val holidayRoomModule: Module
    get() = HolidayRoomDatabaseModule().module
internal actual val diaryRoomModule: Module
    get() = DiaryRoomDatabaseModule().module
