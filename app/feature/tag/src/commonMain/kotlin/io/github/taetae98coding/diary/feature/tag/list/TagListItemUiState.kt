package io.github.taetae98coding.diary.feature.tag.list

import io.github.taetae98coding.diary.core.compose.runtime.SkipProperty

internal data class TagListItemUiState(
    val id: String,
    val title: String,
    val finish: SkipProperty<() -> Unit>,
    val delete: SkipProperty<() -> Unit>,
)
