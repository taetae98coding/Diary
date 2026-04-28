package io.github.taetae98coding.diary.core.location.compose

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
public actual fun rememberLocationPermissionRequester(onResult: (isGranted: Boolean) -> Unit): LocationPermissionRequester {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = onResult,
    )

    return remember(context, launcher) {
        object : LocationPermissionRequester {
            override fun request() {
                val isGranted = context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                if (!isGranted) {
                    launcher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                }
            }
        }
    }
}
