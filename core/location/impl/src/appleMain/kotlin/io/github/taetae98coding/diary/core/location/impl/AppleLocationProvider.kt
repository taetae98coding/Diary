package io.github.taetae98coding.diary.core.location.impl

import io.github.taetae98coding.diary.core.location.api.Location
import io.github.taetae98coding.diary.core.location.api.LocationProvider
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.Foundation.NSError
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
internal class AppleLocationProvider : LocationProvider {
    override val currentLocation: Flow<Location?> = callbackFlow {
        val manager = CLLocationManager()
        val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
            override fun locationManager(
                manager: CLLocationManager,
                didUpdateLocations: List<*>,
            ) {
                val cLLocation = didUpdateLocations.lastOrNull() as? CLLocation
                if (cLLocation != null) {
                    val location = cLLocation.coordinate.useContents { Location(latitude = latitude, longitude = longitude) }
                    trySend(location)
                    close()
                }
            }

            override fun locationManager(
                manager: CLLocationManager,
                didFailWithError: NSError,
            ) {
                trySend(null)
                close()
            }
        }

        manager.delegate = delegate
        when (manager.authorizationStatus) {
            kCLAuthorizationStatusAuthorizedAlways,
            kCLAuthorizationStatusAuthorizedWhenInUse,
            -> {
                manager.requestLocation()
            }

            else -> {
                trySend(null)
                close()
            }
        }

        awaitClose {
            manager.stopUpdatingLocation()
            manager.delegate = null
        }
    }
}
