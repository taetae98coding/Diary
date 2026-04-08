package io.github.taetae98coding.diary.feature.routine.add.card

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.retain.retain
import io.github.taetae98coding.diary.core.model.routine.RoutineRRule

@Stable
internal class RRuleCardState(
    initialRRules: List<RoutineRRule> = emptyList(),
    val typeSelectorState: RRuleTypeSelectorState,
) {
    private var _rRules = mutableStateSetOf(elements = initialRRules.toTypedArray())
    val rRules: List<RoutineRRule>
        get() = _rRules.toList()

    fun addRRule(rRule: List<RoutineRRule>) {
        _rRules += rRule
    }

    fun removeRRule(rRule: RoutineRRule) {
        _rRules -= rRule
    }

    fun clear() {
        _rRules.clear()
    }
}

@Composable
internal fun rememberRRuleCardState(
    vararg inputs: Any?,
    initialRRules: List<RoutineRRule> = emptyList(),
): RRuleCardState {
    val typeSelectorState = rememberRRuleTypeSelectorState()

    return retain(inputs, typeSelectorState) {
        RRuleCardState(
            initialRRules = initialRRules,
            typeSelectorState = typeSelectorState,
        )
    }
}
