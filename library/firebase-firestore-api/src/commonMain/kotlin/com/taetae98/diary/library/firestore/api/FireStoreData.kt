package com.taetae98.diary.library.firestore.api

import kotlinx.datetime.Instant

public interface FireStoreData {
    public fun getBoolean(key: String): Boolean?
    public fun getLong(key: String): Long?
    public fun getString(key: String): String?
    public fun getInstant(key: String): Instant?
}
