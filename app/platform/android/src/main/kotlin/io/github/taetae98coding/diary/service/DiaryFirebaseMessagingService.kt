package io.github.taetae98coding.diary.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.github.taetae98coding.diary.domain.fcm.usecase.UpdateFCMTokenUseCase
import io.github.taetae98coding.diary.notification.DefaultNotificationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

public class DiaryFirebaseMessagingService : FirebaseMessagingService() {
	private val serviceScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

	private val defaultNotificationManager by inject<DefaultNotificationManager>()
	private val updateFCMTokenUseCase by inject<UpdateFCMTokenUseCase>()

	override fun onNewToken(token: String) {
		super.onNewToken(token)
		serviceScope.launch { updateFCMTokenUseCase() }
	}

	override fun onMessageReceived(message: RemoteMessage) {
		super.onMessageReceived(message)
		when (message.data[TYPE]) {
			else -> {
				defaultNotificationManager.notify(
					title = message.notification?.title.orEmpty(),
					description = message.notification?.body,
				)
			}
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		serviceScope.cancel()
	}

	public companion object {
		private const val TYPE = "type"
	}
}
