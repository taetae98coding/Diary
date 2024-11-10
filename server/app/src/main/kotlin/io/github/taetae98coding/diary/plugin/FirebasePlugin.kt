package io.github.taetae98coding.diary.plugin

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.server.application.Application
import java.io.FileInputStream

internal fun Application.installFirebase() {
	val keyPath = environment.config.property("firebase.key").getString()
	val key = FileInputStream(keyPath)
	val options =
		FirebaseOptions
			.builder()
			.setCredentials(GoogleCredentials.fromStream(key))
			.build()

	FirebaseApp.initializeApp(options)
}
