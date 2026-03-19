package io.github.taetae98coding.diary.app.shared

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.defaultPopTransitionSpec
import androidx.navigation3.ui.defaultPredictivePopTransitionSpec
import androidx.navigation3.ui.defaultTransitionSpec
import androidx.navigationevent.NavigationEvent

private const val DEFAULT_TRANSITION_DURATION_MILLISECOND = 700

private fun Scene<*>.isTopLevel(state: AppState): Boolean {
    return key in state.topLevelNavKeys.mapNotNull { it::class.simpleName }.toSet()
}

internal actual fun <T : Any> platformTransitionSpec(state: AppState): AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform {
    return {
        if (initialState.isTopLevel(state) && targetState.isTopLevel(state)) {
            ContentTransform(
                fadeIn(animationSpec = tween(DEFAULT_TRANSITION_DURATION_MILLISECOND)),
                fadeOut(animationSpec = tween(DEFAULT_TRANSITION_DURATION_MILLISECOND)),
            )
        } else {
            defaultTransitionSpec<T>().invoke(this)
        }
    }
}

internal actual fun <T : Any> platformPopTransitionSpec(state: AppState): AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform {
    return {
        if (initialState.isTopLevel(state) && targetState.isTopLevel(state)) {
            ContentTransform(
                fadeIn(animationSpec = tween(DEFAULT_TRANSITION_DURATION_MILLISECOND)),
                fadeOut(animationSpec = tween(DEFAULT_TRANSITION_DURATION_MILLISECOND)),
            )
        } else {
            defaultPopTransitionSpec<T>().invoke(this)
        }
    }
}

internal actual fun <T : Any> platformPredictivePopTransitionSpec(state: AppState): AnimatedContentTransitionScope<Scene<T>>.(@NavigationEvent.SwipeEdge Int) -> ContentTransform {
    return {
        if (initialState.isTopLevel(state) && targetState.isTopLevel(state)) {
            ContentTransform(
                fadeIn(
                    spring(
                        dampingRatio = 1.0F,
                        stiffness = 1600.0F,
                    ),
                ),
                scaleOut(targetScale = 0.7F),
            )
        } else {
            defaultPredictivePopTransitionSpec<T>().invoke(this, it)
        }
    }
}
