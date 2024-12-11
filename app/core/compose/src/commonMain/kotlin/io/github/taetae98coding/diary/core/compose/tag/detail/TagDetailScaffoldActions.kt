package io.github.taetae98coding.diary.core.compose.tag.detail

public sealed class TagDetailScaffoldActions {
	public data object None : TagDetailScaffoldActions()

	public data class FinishAndDelete(
		val isFinish: Boolean = false,
		val finish: () -> Unit = {},
		val restart: () -> Unit = {},
		val delete: () -> Unit = {},
	) : TagDetailScaffoldActions()
}
