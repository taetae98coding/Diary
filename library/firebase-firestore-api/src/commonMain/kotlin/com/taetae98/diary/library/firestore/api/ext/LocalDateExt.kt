package com.taetae98.diary.library.firestore.api.ext

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn

public fun LocalDate.toFireStoreTimestamp(): Any {
    return atStartOfDayIn(TimeZone.UTC).toFireStoreTimestamp()
}