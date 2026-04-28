package io.github.taetae98coding.diary.core.location.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
public actual fun rememberLocationPermissionRequester(onResult: (isGranted: Boolean) -> Unit): LocationPermissionRequester {
    return remember {
        object : LocationPermissionRequester {
            override fun request() = Unit
        }
    }
}
