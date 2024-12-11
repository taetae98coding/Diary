package io.github.taetae98coding.diary.data.fcm.repository

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import io.github.taetae98coding.diary.core.database.FCMTokenTable
import io.github.taetae98coding.diary.domain.fcm.repository.FCMRepository
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.koin.core.annotation.Factory

@Factory
internal class FCMRepositoryImpl : FCMRepository {
	override suspend fun upsert(token: String, owner: String) {
		newSuspendedTransaction {
			FCMTokenTable.upsert(token, owner)
		}
	}

	override suspend fun delete(token: String) {
		newSuspendedTransaction {
			FCMTokenTable.delete(token)
		}
	}

	override suspend fun send(owner: String, title: String, message: String?) {
		val messageList = newSuspendedTransaction {
			FCMTokenTable.findByOwner(owner)
		}.map {
			val notification = Notification
				.builder()
				.setTitle(title)
				.setBody(message)
				.build()

			Message
				.builder()
				.setNotification(notification)
				.setToken(it)
				.build()
		}

		FirebaseMessaging.getInstance().sendEachAsync(messageList)
	}
}
