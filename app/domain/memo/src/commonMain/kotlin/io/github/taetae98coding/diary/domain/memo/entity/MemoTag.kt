package io.github.taetae98coding.diary.domain.memo.entity

import io.github.taetae98coding.diary.core.model.tag.Tag

public data class MemoTag(val tag: Tag, val isSelected: Boolean, val isPrimary: Boolean)
