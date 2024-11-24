package io.github.taetae98coding.diary.core.model.memo

import kotlinx.datetime.LocalDate

public data class MemoDetail(val title: String, val description: String, val start: LocalDate?, val endInclusive: LocalDate?, val color: Int)
