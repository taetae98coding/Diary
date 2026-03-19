package io.github.taetae98coding.diary.compose.calendar.week

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset

public interface CalendarWeekGridItemScope {
    public fun Modifier.animateItem(
        fadeInSpec: FiniteAnimationSpec<Float>? = spring(stiffness = Spring.StiffnessMediumLow),
        placementSpec: FiniteAnimationSpec<IntOffset>? =
            spring(
                stiffness = Spring.StiffnessMediumLow,
                visibilityThreshold = IntOffset.VisibilityThreshold,
            ),
        fadeOutSpec: FiniteAnimationSpec<Float>? = spring(stiffness = Spring.StiffnessMediumLow),
    ): Modifier
}
