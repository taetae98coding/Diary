package io.github.taetae98coding.diary.initializer

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.startup.Initializer
import io.github.taetae98coding.diary.app.manager.FetchManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

public class FetchManagerInitializer :
	Initializer<FetchManager>,
	KoinComponent {
	private val manager by inject<FetchManager>()
	private val appLifecycleOwner by inject<LifecycleOwner>()

	override fun create(context: Context): FetchManager {
		manager.attach(appLifecycleOwner)

		return manager
	}

	override fun dependencies(): MutableList<Class<out Initializer<*>>> = mutableListOf(KoinInitializer::class.java)
}
