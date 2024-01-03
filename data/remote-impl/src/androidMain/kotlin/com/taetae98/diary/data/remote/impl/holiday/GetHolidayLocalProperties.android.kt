package com.taetae98.diary.data.remote.impl.holiday

import com.taetae98.diary.data.remote.impl.BuildConfig

internal actual fun getHolidayUrl(): String {
    return BuildConfig.OPEN_API_HOLIDAY_URL
}

internal actual fun getHolidayKey(): String {
    return BuildConfig.OPEN_API_HOLIDAY_KEY
}