package io.github.taetae98coding.diary.core.location.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Composable
public actual fun rememberLocationPermissionRequester(onResult: (isGranted: Boolean) -> Unit): LocationPermissionRequester {
    val onResult by rememberUpdatedState(onResult)

    return remember {
        val manager = CLLocationManager()
        var didRequest = false

        manager.delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
            override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
                if (!didRequest) return
                didRequest = false

                val status = manager.authorizationStatus
                val isGranted = status == kCLAuthorizationStatusAuthorizedAlways || status == kCLAuthorizationStatusAuthorizedWhenInUse

                onResult(isGranted)
            }
        }

        object : LocationPermissionRequester {
            override fun request() {
                if (manager.authorizationStatus == kCLAuthorizationStatusNotDetermined) {
                    didRequest = true
                    manager.requestWhenInUseAuthorization()
                }
            }
        }
    }
}
