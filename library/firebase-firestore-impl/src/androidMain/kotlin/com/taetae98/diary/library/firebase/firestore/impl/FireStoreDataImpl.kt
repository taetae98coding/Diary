package com.taetae98.diary.library.firebase.firestore.impl

import com.google.firebase.firestore.DocumentSnapshot
import com.taetae98.diary.library.firestore.api.FireStoreData
import kotlinx.datetime.Instant

internal data class FireStoreDataImpl(
    private val documentSnapshot: DocumentSnapshot
) : FireStoreData {
    override fun getString(key: String): String? {
        return documentSnapshot.getString(key)
    }

    override fun getLong(key: String): Long? {
        return documentSnapshot.getLong(key)
    }

    override fun getInstant(key: String): Instant? {
        return documentSnapshot.getTimestamp(key)?.let {
            Instant.fromEpochSeconds(it.seconds, it.nanoseconds)
        }
    }
}
