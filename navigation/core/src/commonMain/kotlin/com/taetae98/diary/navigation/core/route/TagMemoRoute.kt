package com.taetae98.diary.navigation.core.route

import kotlinx.serialization.Serializable

@Serializable
internal data class TagMemoRoute(
    val tagId: String,
) : Route