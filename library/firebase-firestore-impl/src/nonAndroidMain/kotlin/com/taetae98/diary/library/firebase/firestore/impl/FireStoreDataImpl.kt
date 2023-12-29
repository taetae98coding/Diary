package com.taetae98.diary.library.firebase.firestore.impl

import com.taetae98.diary.library.firestore.api.FireStoreData
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.datetime.Instant

internal class FireStoreDataImpl(
    private val documentSnapshot: DocumentSnapshot
) : FireStoreData {
    override fun getString(key: String): String {
        return documentSnapshot.get(key)
    }

    override fun getLong(key: String): Long {
        return documentSnapshot.get(key)
    }

    override fun getInstant(key: String): Instant {
        val timestamp = documentSnapshot.get<Timestamp>(key)
        return Instant.fromEpochSeconds(timestamp.seconds, timestamp.nanoseconds)
    }
}
