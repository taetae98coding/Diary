package io.github.taetae98coding.diary.feature.calendar.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.presenter.calendar.api.CalendarMemoStateHolder
import io.github.taetae98coding.diary.presenter.calendar.compose.CalendarScaffold
import kotlin.uuid.Uuid
import kotlinx.datetime.LocalDateRange
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun CalendarHomeScreen(
    navigateToMemoAdd: (LocalDateRange) -> Unit,
    navigateToMemoDetail: (Uuid) -> Unit,
    memoStateHolder: CalendarMemoStateHolder,
    modifier: Modifier = Modifier,
    viewModel: CalendarHomeViewModel = koinViewModel(),
) {
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()

    CalendarScaffold(
        memoStateHolder = memoStateHolder,
        modifier = modifier,
        isFetchingProvider = { isSyncing },
        onFetch = viewModel::sync,
        onLocalDateRangeSelect = navigateToMemoAdd,
        onMemoClick = navigateToMemoDetail,
    )
}
