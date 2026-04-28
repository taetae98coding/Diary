package io.github.taetae98coding.diary.core.location.impl

import io.github.taetae98coding.diary.core.location.api.Location
import io.github.taetae98coding.diary.core.location.api.LocationProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class JvmLocationProvider : LocationProvider {
    override val currentLocation: Flow<Location?> = flowOf(null)
}
