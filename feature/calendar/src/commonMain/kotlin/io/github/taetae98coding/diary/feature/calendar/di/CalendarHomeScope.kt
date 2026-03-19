package io.github.taetae98coding.diary.feature.calendar.di

import org.koin.core.annotation.Qualifier

@Qualifier
internal annotation class CalendarHomeScope {
    companion object {
        const val DEFAULT_ID = "CalendarHomeScope"
    }
}
