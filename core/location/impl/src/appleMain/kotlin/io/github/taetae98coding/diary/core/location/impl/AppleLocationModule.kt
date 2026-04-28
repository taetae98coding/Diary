package io.github.taetae98coding.diary.core.location.impl

import io.github.taetae98coding.diary.core.location.api.LocationProvider
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
public class AppleLocationModule {
    @Single
    internal fun providesLocationProvider(): LocationProvider {
        return AppleLocationProvider()
    }
}
