package io.github.taetae98coding.diary.core.model.memo

import kotlinx.datetime.Instant

public data class Memo(val id: String, val detail: MemoDetail, val primaryTag: String?, val owner: String?, val isFinish: Boolean, val isDelete: Boolean, val updateAt: Instant)
