package io.github.taetae98coding.diary.feature.routine.add.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.compose.core.dialog.DialogState
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.core.model.routine.RoutineRRule

@Composable
internal fun RRuleEditorDialog(
    state: DialogState,
    onConfirm: (RoutineRRule) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (!state.isVisible) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val byDayState = rememberByDayEditorState()
    val byMonthDayState = rememberByMonthDayEditorState()

    ModalBottomSheet(
        onDismissRequest = state::hide,
        sheetState = sheetState,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "반복 규칙 추가",
                style = DiaryTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ByDayEditor(state = byDayState, modifier = Modifier.fillMaxWidth())
                ByMonthDayEditor(state = byMonthDayState, modifier = Modifier.fillMaxWidth())
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(onClick = state::hide) {
                    Text(text = "취소")
                }
                TextButton(
                    onClick = {
                        onConfirm(
                            RoutineRRule(
                                diaryByDay = byDayState.diaryByDay,
                                byMonthDay = byMonthDayState.byMonthDay,
                            ),
                        )
                    },
                ) {
                    Text(text = "확인")
                }
            }
        }
    }
}
