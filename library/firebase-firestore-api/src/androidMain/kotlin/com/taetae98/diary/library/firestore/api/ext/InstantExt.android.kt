package com.taetae98.diary.library.firestore.api.ext

import com.google.firebase.Timestamp
import kotlinx.datetime.Instant

public actual fun Instant.toFireStoreTimestamp(): Any {
    return Timestamp(epochSeconds, nanosecondsOfSecond)
}