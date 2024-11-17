package io.github.taetae98coding.diary.feature.tag.detail

internal sealed class TagDetailActionButton {
    data object None : TagDetailActionButton()

    data class FinishAndDetail(
        val isFinish: Boolean,
        val onFinishChange: (Boolean) -> Unit,
        val delete: () -> Unit,
    ) : TagDetailActionButton()
}
