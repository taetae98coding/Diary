package io.github.taetae98coding.diary.core.diary.service.plugin

import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.header
import kotlinx.coroutines.flow.first

internal val DiaryClientTokenPlugin = createClientPlugin(
    name = "DiaryClientTokenPlugin",
    createConfiguration = { AccountPreferencesOwner() },
    body = {
        val owner = pluginConfig

        onRequest { request, _ ->
            owner.preferences?.getToken()
                ?.first()
                ?.let { request.header("Authorization", "Bearer $it") }
        }
    },
)
