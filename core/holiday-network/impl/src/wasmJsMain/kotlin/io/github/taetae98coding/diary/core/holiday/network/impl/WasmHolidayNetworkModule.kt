package io.github.taetae98coding.diary.core.holiday.network.impl

import io.github.taetae98coding.diary.core.holiday.network.impl.di.HolidayHttpEngine
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
public class WasmHolidayNetworkModule {
    @HolidayHttpEngine
    @Single
    internal fun providesHolidayHttpEngine(): HttpClientEngine {
        return Js.create()
    }
}
