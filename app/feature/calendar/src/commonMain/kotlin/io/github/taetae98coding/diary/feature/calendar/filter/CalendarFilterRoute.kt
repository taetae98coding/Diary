package io.github.taetae98coding.diary.feature.calendar.filter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun CalendarFilterRoute(
	navigateUp: () -> Unit,
	modifier: Modifier = Modifier,
	tagViewModel: CalendarFilterTagViewModel = koinViewModel(),
) {
	val tagList by tagViewModel.list.collectAsStateWithLifecycle()

	CalendarFilterDialog(
		onDismissRequest = navigateUp,
		listProvider = { tagList },
		modifier = modifier,
	)
}
