package com.taetae98.diary.data.remote.impl.holiday

import com.taetae98.diary.data.remote.impl.BuildKonfig

internal actual fun getHolidayUrl(): String {
    return BuildKonfig.OPEN_API_HOLIDAY_URL
}

internal actual fun getHolidayKey(): String {
    return BuildKonfig.OPEN_API_HOLIDAY_KEY
}