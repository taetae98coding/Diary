package io.github.taetae98coding.diary.app.shared

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.defaultPopTransitionSpec
import androidx.navigation3.ui.defaultPredictivePopTransitionSpec
import androidx.navigation3.ui.defaultTransitionSpec
import androidx.navigationevent.NavigationEvent

internal actual fun <T : Any> platformTransitionSpec(state: io.github.taetae98coding.diary.app.shared.AppState): androidx.compose.animation.AnimatedContentTransitionScope<androidx.navigation3.scene.Scene<T>>.() -> androidx.compose.animation.ContentTransform {
    return defaultTransitionSpec()
}

internal actual fun <T : Any> platformPopTransitionSpec(state: AppState): AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform {
    return defaultPopTransitionSpec()
}

internal actual fun <T : Any> platformPredictivePopTransitionSpec(state: AppState): AnimatedContentTransitionScope<Scene<T>>.(@NavigationEvent.SwipeEdge Int) -> ContentTransform {
    return defaultPredictivePopTransitionSpec()
}
