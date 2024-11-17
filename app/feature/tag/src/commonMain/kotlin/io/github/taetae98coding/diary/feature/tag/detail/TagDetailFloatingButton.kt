package io.github.taetae98coding.diary.feature.tag.detail

internal sealed class TagDetailFloatingButton {
    data object None : TagDetailFloatingButton()
    data class Add(val onAdd: () -> Unit) : TagDetailFloatingButton()
}
