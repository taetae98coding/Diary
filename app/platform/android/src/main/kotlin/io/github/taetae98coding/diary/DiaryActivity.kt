package io.github.taetae98coding.diary

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import io.github.taetae98coding.diary.app.App

public class DiaryActivity : ComponentActivity() {
	private val notificationPermissionLauncher =
		registerForActivityResult(
			contract = ActivityResultContracts.RequestPermission(),
			callback = {},
		)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			App()
		}

		notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
	}
}
