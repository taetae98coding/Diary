package io.github.taetae98coding.diary.core.model

import kotlinx.datetime.Instant

public data class Tag(val id: String, val title: String, val description: String, val color: Int, val owner: String, val isFinish: Boolean, val isDelete: Boolean, val updateAt: Instant)
