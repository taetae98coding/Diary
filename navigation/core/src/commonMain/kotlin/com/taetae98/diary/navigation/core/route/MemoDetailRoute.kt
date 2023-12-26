package com.taetae98.diary.navigation.core.route

import kotlinx.serialization.Serializable

@Serializable
internal data class MemoDetailRoute(
    val memoId: String
) : Route
