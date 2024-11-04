package io.github.taetae98coding.diary.initializer

import io.github.taetae98coding.diary.BuildKonfig
import io.github.taetae98coding.diary.app.AppModule
import io.github.taetae98coding.diary.core.diary.database.memory.DiaryMemoryDatabaseModule
import io.github.taetae98coding.diary.core.diary.service.DiaryServiceModule
import io.github.taetae98coding.diary.core.holiday.database.memory.HolidayMemoryDatabaseModule
import io.github.taetae98coding.diary.core.holiday.preferences.memory.HolidayPreferencesMemoryModule
import io.github.taetae98coding.diary.core.holiday.service.HolidayServiceModule
import io.github.taetae98coding.diary.fore.account.preferences.memory.AccountPreferencesMemoryModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import org.koin.ksp.generated.module

internal fun initKoin(): KoinApplication {
    return startKoin {
        modules(
            AppModule().module,
            diaryServiceModule(),
            AccountPreferencesMemoryModule().module,
            HolidayPreferencesMemoryModule().module,
            HolidayMemoryDatabaseModule().module,
            holidayServiceModule(),
            DiaryMemoryDatabaseModule().module,
        )
    }
}

private fun diaryServiceModule(): Module {
    return module {
        single(qualifier = StringQualifier(DiaryServiceModule.DIARY_API_URL)) { BuildKonfig.DIARY_API_URL }
    }
}

private fun holidayServiceModule(): Module {
    return module {
        single(qualifier = StringQualifier(HolidayServiceModule.HOLIDAY_API_URL)) { BuildKonfig.HOLIDAY_API_URL }
        single(qualifier = StringQualifier(HolidayServiceModule.HOLIDAY_API_KEY)) { BuildKonfig.HOLIDAY_API_KEY }
    }
}
