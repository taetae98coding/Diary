package io.github.taetae98coding.diary.core.ip.network.impl

import io.github.taetae98coding.diary.core.ip.network.impl.di.IpHttpEngine
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
public class WasmIpNetworkModule {
    @IpHttpEngine
    @Single
    internal fun providesIpHttpEngine(): HttpClientEngine {
        return Js.create()
    }
}
