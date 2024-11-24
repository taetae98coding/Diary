package io.github.taetae98coding.diary.library.firebase.messaging

import cocoapods.FirebaseMessaging.FIRMessaging
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalForeignApi::class)
internal class KFirebaseMessagingImpl : KFirebaseMessaging {
	override suspend fun getToken(): String =
		suspendCancellableCoroutine { continuation ->
			FIRMessaging.messaging().tokenWithCompletion { token, error ->
				if (error != null) {
					continuation.resumeWithException(Exception(error.toString()))
				} else if (token.isNullOrBlank()) {
					continuation.resumeWithException(Exception("token is null or blank"))
				} else {
					continuation.resume(token)
				}
			}
		}
}
