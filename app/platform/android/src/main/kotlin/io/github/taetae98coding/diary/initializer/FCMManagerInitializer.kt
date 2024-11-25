package io.github.taetae98coding.diary.initializer

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.startup.Initializer
import io.github.taetae98coding.diary.app.manager.FCMManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

public class FCMManagerInitializer :
	Initializer<FCMManager>,
	KoinComponent {
	private val manager by inject<FCMManager>()
	private val appLifecycleOwner by inject<LifecycleOwner>()

	override fun create(context: Context): FCMManager {
		manager.attach(appLifecycleOwner)

		return manager
	}

	override fun dependencies(): MutableList<Class<out Initializer<*>>> = mutableListOf(KoinInitializer::class.java)
}
