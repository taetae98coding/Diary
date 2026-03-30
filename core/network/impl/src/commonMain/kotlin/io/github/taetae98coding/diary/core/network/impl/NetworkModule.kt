package io.github.taetae98coding.diary.core.network.impl

import io.github.taetae98coding.diary.core.network.impl.di.ApiJson
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan
@Configuration
public class NetworkModule {
    @ApiJson
    @Single
    internal fun providesApiJson(): Json {
        return Json {
            encodeDefaults = true
        }
    }
}
