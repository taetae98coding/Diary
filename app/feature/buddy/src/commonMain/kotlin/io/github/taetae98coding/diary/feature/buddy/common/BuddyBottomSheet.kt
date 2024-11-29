package io.github.taetae98coding.diary.feature.buddy.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.core.design.system.icon.AccountIcon
import io.github.taetae98coding.diary.core.design.system.icon.SearchIcon
import io.github.taetae98coding.diary.core.design.system.text.ClearTextField
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.library.color.multiplyAlpha

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BuddyBottomSheet(
	state: BuddyBottomSheetState,
	onDismissRequest: () -> Unit,
	uiStateProvider: () -> BuddyBottomSheetUiState,
	modifier: Modifier = Modifier,
) {
	ModalBottomSheet(
		onDismissRequest = onDismissRequest,
		modifier = modifier,
	) {
		ClearTextField(
			valueProvider = { state.email },
			onValueChange = state::onEmailChange,
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = DiaryTheme.dimen.diaryHorizontalPadding),
			placeholder = { Text(text = "이메일") },
			leadingIcon = { SearchIcon() },
			shape = CircleShape,
		)

		LazyColumn(
			modifier = Modifier.fillMaxSize(),
			contentPadding = PaddingValues(DiaryTheme.dimen.itemSpace),
			verticalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace),
		) {
			if (state.email.isNotBlank()) {
				val uiState = uiStateProvider()

				when (uiState) {
					is BuddyBottomSheetUiState.State -> {
						if (uiState.buddyList.isEmpty()) {
							item(contentType = "MessageBox") {
								MessageBox(
									message = "계정이 존재하지 않아요 🐹",
									modifier = Modifier.animateItem(),
								)
							}
						} else {
							items(
								items = uiState.buddyList,
								key = { it.uid },
								contentType = { "Buddy" },
							) {
								BuddyItem(
									uiState = it,
									modifier = Modifier.animateItem(),
								)
							}
						}
					}

					is BuddyBottomSheetUiState.NetworkError -> {
						item(contentType = "MessageBox") {
							MessageBox(
								message = "네트워크 오류 🐹",
								modifier = Modifier.animateItem(),
							)
						}
					}

					is BuddyBottomSheetUiState.Loading -> {
						items(
							count = 10,
							contentType = { "Buddy" },
						) {
							BuddyItem(
								uiState = null,
								modifier = Modifier.animateItem(),
							)
						}
					}
				}
			}
		}
	}
}

@Composable
private fun MessageBox(
	message: String,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier
			.fillMaxWidth()
			.heightIn(min = 150.dp),
		contentAlignment = Alignment.Center,
	) {
		Text(text = message)
	}
}

@Composable
private fun BuddyItem(
	uiState: BuddyUiState?,
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = Modifier
			.clip(CircleShape)
			.toggleable(
				value = uiState?.isSelected == true,
				role = Role.Checkbox,
				onValueChange = {
					if (it) {
						uiState?.select?.value?.invoke()
					} else {
						uiState?.unselect?.value?.invoke()
					}
				},
			).fillMaxWidth()
			.minimumInteractiveComponentSize()
			.padding(8.dp),
		horizontalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace),
		verticalAlignment = Alignment.CenterVertically,
	) {
		Box(
			modifier = modifier
				.size(48.dp)
				.background(
					color = LocalContentColor.current.multiplyAlpha(value = 0.38F),
					shape = CircleShape,
				),
			contentAlignment = Alignment.Center,
		) {
			AccountIcon(modifier = Modifier.size(24.dp))
		}
		Text(text = uiState?.email.orEmpty())
		Spacer(modifier = Modifier.weight(1F))
		Checkbox(
			checked = uiState?.isSelected == true,
			onCheckedChange = null,
		)
	}
}
