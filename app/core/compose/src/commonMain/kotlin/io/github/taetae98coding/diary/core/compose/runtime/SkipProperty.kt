package io.github.taetae98coding.diary.core.compose.runtime

import androidx.compose.runtime.Immutable

@Immutable
public class SkipProperty<T>(
	public val value: T,
) {
	override fun equals(other: Any?): Boolean = true

	override fun hashCode(): Int = 0
}
