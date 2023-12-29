package com.taetae98.diary.library.firestore.api.ext

import kotlinx.datetime.Instant

public actual fun Instant.toFireStoreTimestamp(): Any {
    return Unit
}

public actual fun Any.toFireStoreInstant(): Instant {
    return Instant.fromEpochSeconds(0L)
}
