package io.github.taetae98coding.diary.notification

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.github.taetae98coding.diary.R
import org.koin.core.annotation.Factory
import kotlin.math.abs

@Factory
internal class DefaultNotificationManager(
	private val context: Context,
) {
	fun notify(title: String, description: String?) {
		if (context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return

		val notification =
			NotificationCompat
				.Builder(context, CHANNEL_ID)
				.setSmallIcon(R.drawable.ic_android_black_24dp)
				.setContentTitle(title)
				.setContentText(description)
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)
				.build()

		val channel =
			NotificationChannelCompat
				.Builder(CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_LOW)
				.setName(context.getString(R.string.default_channel_name))
				.setDescription(context.getString(R.string.default_channel_description))
				.build()

		val manager = NotificationManagerCompat.from(context)

		manager.createNotificationChannel(channel)
		manager.notify(abs(System.currentTimeMillis().toInt()), notification)
	}

	companion object {
		const val CHANNEL_ID = "Diary"
	}
}
