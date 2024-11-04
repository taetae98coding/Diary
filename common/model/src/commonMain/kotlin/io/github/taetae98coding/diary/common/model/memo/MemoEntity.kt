package io.github.taetae98coding.diary.common.model.memo

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class MemoEntity(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("start")
    val start: LocalDate?,
    @SerialName("endInclusive")
    val endInclusive: LocalDate?,
    @SerialName("color")
    val color: Int,
    @SerialName("owner")
    val owner: String,
    @SerialName("isFinish")
    val isFinish: Boolean,
    @SerialName("isDelete")
    val isDelete: Boolean,
    @SerialName("updateAt")
    val updateAt: Instant,
)
