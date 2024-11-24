package io.github.taetae98coding.diary.library.firebase.messaging

import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.tasks.await

internal class KFirebaseMessagingImpl : KFirebaseMessaging {
	override suspend fun getToken(): String = Firebase.messaging.token.await()
}
