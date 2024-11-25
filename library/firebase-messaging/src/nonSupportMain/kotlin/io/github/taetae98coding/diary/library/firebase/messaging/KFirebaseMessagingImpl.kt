package io.github.taetae98coding.diary.library.firebase.messaging

internal class KFirebaseMessagingImpl : KFirebaseMessaging {
	override suspend fun getToken(): String {
		error("Not Support")
	}
}
