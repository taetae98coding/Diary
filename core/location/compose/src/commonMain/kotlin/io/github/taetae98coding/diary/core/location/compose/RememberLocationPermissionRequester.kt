package io.github.taetae98coding.diary.core.location.compose

import androidx.compose.runtime.Composable

@Composable
public expect fun rememberLocationPermissionRequester(onResult: (isGranted: Boolean) -> Unit = {}): LocationPermissionRequester
