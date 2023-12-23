package com.taetae98.diary.library.firestore.api.ext

import kotlinx.datetime.Instant

public expect fun Instant.toFireStoreTimestamp(): Any