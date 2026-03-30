package io.github.taetae98coding.diary.core.ip.network.impl

import io.github.taetae98coding.diary.core.ip.network.impl.di.IpHttpEngine
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
public class AppleIpNetworkModule {
    @IpHttpEngine
    @Single
    internal fun providesIpHttpEngine(): HttpClientEngine {
        return Darwin.create()
    }
}
