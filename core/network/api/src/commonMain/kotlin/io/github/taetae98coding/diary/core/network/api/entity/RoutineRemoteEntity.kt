package io.github.taetae98coding.diary.core.network.api.entity

import kotlin.uuid.Uuid
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class RoutineRemoteEntity(
    @SerialName("id")
    val id: Uuid,
    @SerialName("detail")
    val detail: RoutineDetailRemoteEntity,
    @SerialName("rRules")
    val rRules: List<RoutineRRuleRemoteEntity> = emptyList(),
    @SerialName("rDates")
    val rDates: List<LocalDate> = emptyList(),
    @SerialName("exDates")
    val exDates: List<LocalDate> = emptyList(),
    @SerialName("isFinished")
    val isFinished: Boolean,
    @SerialName("isDeleted")
    val isDeleted: Boolean,
    @SerialName("updatedAt")
    val updatedAt: Long,
    @SerialName("createdAt")
    val createdAt: Long,
)
