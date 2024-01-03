package com.taetae98.diary.data.remote.impl.holiday

import org.koin.core.annotation.Module

@Module
internal class HolidayModule {
    companion object {
        const val HOLIDAY_CLIENT = "holidayClient"
        const val HOLIDAY_JSON = "holidayJson"
    }
}