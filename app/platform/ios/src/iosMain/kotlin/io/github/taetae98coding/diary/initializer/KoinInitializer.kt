package io.github.taetae98coding.diary.initializer

import io.github.taetae98coding.diary.BuildKonfig
import io.github.taetae98coding.diary.core.diary.service.DiaryServiceModule
import io.github.taetae98coding.diary.core.holiday.service.HolidayServiceModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module

internal expect val appModule: Module
internal expect val accountDataStoreModule: Module
internal expect val holidayDataStoreModule: Module
internal expect val holidayRoomModule: Module
internal expect val diaryRoomModule: Module

public fun initKoin(): KoinApplication {
    return startKoin {
        modules(
            appModule,
            diaryServiceModule(),
            accountDataStoreModule,
            holidayDataStoreModule,
            holidayRoomModule,
            holidayServiceModule(),
            diaryRoomModule,
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
