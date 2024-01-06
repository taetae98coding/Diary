package com.taetae98.diary.navigation.core.route

import kotlinx.serialization.Serializable

@Serializable
internal class MemoRoute(
    val initialRoute: Route? = null,
) : Route