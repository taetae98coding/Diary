package com.taetae98.diary.data.remote.impl.holiday.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class OpenApiEntity<T>(
    @SerialName("body")
    val body: T
) {
}