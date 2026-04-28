package io.github.taetae98coding.diary.core.location.impl

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import io.github.taetae98coding.diary.core.location.api.Location
import io.github.taetae98coding.diary.core.location.api.LocationProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

internal class AndroidLocationProvider(private val context: Context) : LocationProvider {
    override val currentLocation: Flow<Location?> = flow {
        if (!hasLocationPermission()) {
            emit(null)
            return@flow
        }

        val androidLocation = try {
            val client = LocationServices.getFusedLocationProviderClient(context)
            client.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null).await()
        } catch (cause: Throwable) {
            null
        }

        emit(androidLocation?.let { Location(latitude = it.latitude, longitude = it.longitude) })
    }

    private fun hasLocationPermission(): Boolean {
        val coarse = context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        val fine = context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        return coarse == PackageManager.PERMISSION_GRANTED || fine == PackageManager.PERMISSION_GRANTED
    }
}
