package io.github.taetae98coding.diary.initializer

import android.content.Context
import androidx.startup.Initializer
import io.github.taetae98coding.diary.BuildConfig
import io.github.taetae98coding.diary.app.AppModule
import io.github.taetae98coding.diary.core.account.preferences.datastore.AccountDataStorePreferencesModule
import io.github.taetae98coding.diary.core.diary.database.room.DiaryRoomDatabaseModule
import io.github.taetae98coding.diary.core.diary.service.DiaryServiceModule
import io.github.taetae98coding.diary.core.holiday.database.room.HolidayRoomDatabaseModule
import io.github.taetae98coding.diary.core.holiday.preferences.datastore.HolidayDataStorePreferencesModule
import io.github.taetae98coding.diary.core.holiday.service.HolidayServiceModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import org.koin.ksp.generated.module

public class KoinInitializer : Initializer<KoinApplication> {
    override fun create(context: Context): KoinApplication {
        return startKoin {
            androidContext(context)

            modules(
                AppModule().module,
                diaryServiceModule(),
                AccountDataStorePreferencesModule().module,
                HolidayDataStorePreferencesModule().module,
                HolidayRoomDatabaseModule().module,
                holidayServiceModule(),
                DiaryRoomDatabaseModule().module,
            )
        }
    }

    private fun diaryServiceModule(): Module {
        return module {
            single(qualifier = StringQualifier(DiaryServiceModule.DIARY_API_URL)) { BuildConfig.DIARY_API_URL }
        }
    }

    private fun holidayServiceModule(): Module {
        return module {
            single(qualifier = StringQualifier(HolidayServiceModule.HOLIDAY_API_URL)) { BuildConfig.HOLIDAY_API_URL }
            single(qualifier = StringQualifier(HolidayServiceModule.HOLIDAY_API_KEY)) { BuildConfig.HOLIDAY_API_KEY }
        }
    }


    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}
