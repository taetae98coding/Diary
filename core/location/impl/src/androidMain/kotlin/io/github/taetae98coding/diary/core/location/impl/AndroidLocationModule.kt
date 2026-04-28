package io.github.taetae98coding.diary.core.location.impl

import android.content.Context
import io.github.taetae98coding.diary.core.location.api.LocationProvider
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
public class AndroidLocationModule {
    @Single
    internal fun providesLocationProvider(context: Context): LocationProvider {
        return AndroidLocationProvider(context)
    }
}
