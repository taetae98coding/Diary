package com.taetae98.diary.library.firebase.firestore.impl

import com.google.firebase.firestore.DocumentSnapshot
import com.taetae98.diary.library.firestore.api.FireStoreData
import kotlinx.datetime.Instant

internal class FireStoreDataImpl(
    private val documentSnapshot: DocumentSnapshot
) : FireStoreData {
    override fun getString(key: String): String {
        return requireNotNull(documentSnapshot.getString(key))
    }

    override fun getLong(key: String): Long {
        return requireNotNull(documentSnapshot.getLong(key))
    }

    override fun getInstant(key: String): Instant {
        val timestamp = requireNotNull(documentSnapshot.getTimestamp(key))
        return Instant.fromEpochSeconds(timestamp.seconds, timestamp.nanoseconds)
    }
}
