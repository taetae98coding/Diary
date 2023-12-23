package com.taetae98.diary.library.firestore.api.ext

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.datetime.Instant

public actual fun Instant.toFireStoreTimestamp(): Any {
    return Timestamp(epochSeconds, nanosecondsOfSecond)
}