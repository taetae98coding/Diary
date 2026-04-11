package io.github.taetae98coding.diary.feature.routine.add.card

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue

internal enum class RRuleType {
    ByDay,
    ByMonthDay,
}

@Stable
internal class RRuleTypeSelectorState {
    var rRuleType: RRuleType by mutableStateOf(RRuleType.ByDay)
        private set

    fun updateRRuleType(value: RRuleType) {
        rRuleType = value
    }
}

@Composable
internal fun rememberRRuleTypeSelectorState(): RRuleTypeSelectorState {
    return retain { RRuleTypeSelectorState() }
}
