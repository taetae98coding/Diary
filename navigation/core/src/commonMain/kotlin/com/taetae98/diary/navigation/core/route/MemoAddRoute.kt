package com.taetae98.diary.navigation.core.route

import kotlinx.serialization.Serializable

@Serializable
internal data class MemoAddRoute(
    val dateRange: ClosedRange<Long>? = null,
    val tagIdSet: Set<String> = emptySet(),
) : Route