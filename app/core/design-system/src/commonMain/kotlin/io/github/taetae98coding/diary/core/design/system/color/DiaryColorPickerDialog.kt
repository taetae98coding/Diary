package io.github.taetae98coding.diary.core.design.system.color

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun DiaryColorPickerDialog(
	initialColor: Color,
	onDismissRequest: () -> Unit,
	onConfirm: (Color) -> Unit,
	modifier: Modifier = Modifier,
) {
	BasicAlertDialog(
		onDismissRequest = onDismissRequest,
		modifier = modifier,
	) {
		Card(
			colors = CardDefaults.cardColors(
				containerColor = AlertDialogDefaults.containerColor,
			),
		) {
			val state = rememberDiaryColorPickerState(initialColor = initialColor)

			DiaryColorPicker(state = state)

			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 6.dp),
				horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
			) {
				TextButton(onClick = onDismissRequest) {
					Text(text = "닫기")
				}
				TextButton(
					onClick = {
						onConfirm(state.color)
						onDismissRequest()
					},
				) {
					Text(text = "선택")
				}
			}
		}
	}
}
