package io.github.taetae98coding.diary.core.ip.network.impl

import io.github.taetae98coding.diary.core.ip.network.api.datasource.IpRemoteDataSource
import io.github.taetae98coding.diary.core.ip.network.impl.di.IpHttpEngine
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.doubles.shouldBeExactly
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import org.koin.plugin.module.dsl.koinApplication

class IpRemoteDataSourceImplTest : FunSpec() {
    init {
        afterTest { stopKoin() }

        test("getLocation success") {
            val responseBody = this::class.java.classLoader
                .getResource("ip/success.json")!!
                .readText()

            val engine = MockEngine {
                respond(
                    content = responseBody,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }

            val koinApp = koinApplication<IpTestKoinApplication> {
                modules(
                    module {
                        single<HttpClientEngine>(qualifier = qualifier(IpHttpEngine::class.simpleName.orEmpty())) { engine }
                    },
                )
            }

            val dataSource = koinApp.koin.get<IpRemoteDataSource>()
            val result = dataSource.get()

            result.latitude shouldBeExactly 11.1111
            result.longitude shouldBeExactly 222.2222
        }
    }
}
