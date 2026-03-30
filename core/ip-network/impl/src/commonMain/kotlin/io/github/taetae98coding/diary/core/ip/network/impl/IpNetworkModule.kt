package io.github.taetae98coding.diary.core.ip.network.impl

import io.github.taetae98coding.diary.core.ip.network.impl.di.IpHttpClient
import io.github.taetae98coding.diary.core.ip.network.impl.di.IpHttpEngine
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.DefaultJson
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan
@Configuration
public class IpNetworkModule {
    @IpHttpClient
    @Single
    internal fun providesIpHttpClient(@IpHttpEngine engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            defaultRequest {
                url("http://ip-api.com/")
            }

            install(ContentNegotiation) {
                json(
                    Json(DefaultJson) {
                        ignoreUnknownKeys = true
                    },
                )
            }
        }
    }
}
