package io.github.taetae98coding.diary.core.location.api

import kotlinx.coroutines.flow.Flow

public interface LocationProvider {
    public val currentLocation: Flow<Location?>
}
