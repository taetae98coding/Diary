package io.github.taetae98coding.diary.feature.memo.detail

internal sealed class MemoDetailFloatingButton {
	data object None : MemoDetailFloatingButton()

	data class Add(val onAdd: () -> Unit) : MemoDetailFloatingButton()
}
