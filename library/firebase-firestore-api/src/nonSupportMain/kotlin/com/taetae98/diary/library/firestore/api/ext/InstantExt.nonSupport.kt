package com.taetae98.diary.library.firestore.api.ext

import kotlinx.datetime.Instant

public actual fun Instant.toFireStoreTimestamp(): Any {
    return this
}
