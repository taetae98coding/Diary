package io.github.taetae98coding.diary.library.firebase.messaging

public interface KFirebaseMessaging {
    public suspend fun getToken(): String
}
