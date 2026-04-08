package io.github.taetae98coding.diary.core.network.api.entity

import kotlin.uuid.Uuid
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class RoutineRemoteEntity(
    @SerialName("id")
    val id: Uuid,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("start")
    val start: String?,
    @SerialName("endInclusive")
    val endInclusive: String?,
    @SerialName("color")
    val color: Int,
    @SerialName("rRules")
    val rRules: String,
    @SerialName("rDates")
    val rDates: String,
    @SerialName("exDates")
    val exDates: String,
    @SerialName("routineCount")
    val routineCount: Int,
    @SerialName("isFinished")
    val isFinished: Boolean,
    @SerialName("isDeleted")
    val isDeleted: Boolean,
    @SerialName("updatedAt")
    val updatedAt: Long,
    @SerialName("createdAt")
    val createdAt: Long,
)
