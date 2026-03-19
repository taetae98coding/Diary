package io.github.taetae98coding.diary.compose.calendar.week

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset

internal class CalendarWeekLazyGridItemScope(private val lazyGridItemScope: LazyGridItemScope) : CalendarWeekGridItemScope {
    override fun Modifier.animateItem(
        fadeInSpec: FiniteAnimationSpec<Float>?,
        placementSpec: FiniteAnimationSpec<IntOffset>?,
        fadeOutSpec: FiniteAnimationSpec<Float>?,
    ): Modifier {
        return with(lazyGridItemScope) {
            animateItem(
                fadeInSpec = fadeInSpec,
                placementSpec = placementSpec,
                fadeOutSpec = fadeOutSpec,
            )
        }
    }
}
