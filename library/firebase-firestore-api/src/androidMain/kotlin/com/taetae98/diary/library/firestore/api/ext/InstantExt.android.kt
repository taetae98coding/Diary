package com.taetae98.diary.library.firestore.api.ext

import com.google.firebase.Timestamp
import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant

public actual fun Instant.toFireStoreTimestamp(): Any {
    return Timestamp(epochSeconds, nanosecondsOfSecond)
}

public actual fun Any.toFireStoreInstant(): Instant {
    val timestamp = this as Timestamp
    return Instant.fromEpochSeconds(timestamp.seconds, timestamp.nanoseconds)
}