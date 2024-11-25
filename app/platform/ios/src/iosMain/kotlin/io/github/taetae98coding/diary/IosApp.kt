package io.github.taetae98coding.diary

import androidx.compose.ui.window.ComposeUIViewController
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import io.github.taetae98coding.diary.app.App
import io.github.taetae98coding.diary.core.coroutines.AppLifecycleOwner
import platform.UIKit.UIViewController

public fun compose(): UIViewController =
	ComposeUIViewController {
		App()

		LifecycleStartEffect(keys = arrayOf(AppLifecycleOwner)) {
			AppLifecycleOwner.start()
			onStopOrDispose { AppLifecycleOwner.stop() }
		}

		LifecycleResumeEffect(keys = arrayOf(AppLifecycleOwner)) {
			AppLifecycleOwner.resume()
			onPauseOrDispose { AppLifecycleOwner.pause() }
		}
	}
