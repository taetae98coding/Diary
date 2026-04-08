package io.github.taetae98coding.diary.feature.more.goldenholiday

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.rememberViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.rememberViewModelStoreProvider
import io.github.taetae98coding.diary.compose.core.button.NavigateUpButton
import io.github.taetae98coding.diary.compose.core.chip.DiaryFilterChip
import io.github.taetae98coding.diary.compose.core.icon.SettingsIcon
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.datetime.LocalDateRange
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun GoldenHolidayScreen(
    navigateUp: () -> Unit,
    navigateToMemoAdd: (LocalDateRange) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberGoldenHolidayScaffoldState()

    GoldenHolidayScreen(
        modifier = modifier,
        state = state,
        onNavigateUp = navigateUp,
        onLocalDateRangeSelect = navigateToMemoAdd,
    )

    SettingDialogHost(state = state)
}

@Composable
private fun GoldenHolidayScreen(
    modifier: Modifier = Modifier,
    state: GoldenHolidayScaffoldState = rememberGoldenHolidayScaffoldState(),
    onNavigateUp: () -> Unit = {},
    onLocalDateRangeSelect: (LocalDateRange) -> Unit = {},
) {
    val provider = rememberViewModelStoreProvider()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                state = state,
                onNavigateUp = onNavigateUp,
            )
        },
    ) { paddingValues ->
        HorizontalPager(
            state = state.pagerState,
            modifier = Modifier.padding(paddingValues)
                .fillMaxSize(),
        ) { year ->
            CompositionLocalProvider(LocalViewModelStoreOwner provides rememberViewModelStoreOwner(provider = provider, key = year)) {
                val viewModel = koinViewModel<GoldenHolidayViewModel> { parametersOf(year) }
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                GoldenHolidayContent(
                    sortOrderProvider = { state.sortOrder },
                    uiStateProvider = { uiState },
                    onRetry = { viewModel.fetch(state.annualLeave) },
                    onLocalDateRangeSelect = onLocalDateRangeSelect,
                )

                LaunchedEffect(state, viewModel) {
                    snapshotFlow { state.annualLeave }
                        .collectLatest(viewModel::fetch)
                }
            }
        }
    }
}

@Composable
private fun TopBar(
    state: GoldenHolidayScaffoldState,
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = { Text(text = "${state.pagerState.currentPage}년 황금연휴") },
        modifier = modifier,
        navigationIcon = { NavigateUpButton(onClick = onNavigateUp) },
        actions = {
            IconButton(onClick = state.dialogState::show) {
                SettingsIcon()
            }
        },
    )
}

@Composable
private fun SettingDialogHost(
    state: GoldenHolidayScaffoldState,
    modifier: Modifier = Modifier,
) {
    if (state.dialogState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = state.dialogState::hide,
            modifier = modifier,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "연차 일수 설정",
                    style = DiaryTheme.typography.titleLarge,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    FilledTonalButton(
                        onClick = state::minusAnnualLeave,
                        enabled = state.annualLeave > 0,
                    ) {
                        Text(text = "-")
                    }

                    Text(
                        text = "${state.annualLeave}일",
                        style = DiaryTheme.typography.headlineMedium,
                    )

                    FilledTonalButton(
                        onClick = state::plusAnnualLeave,
                    ) {
                        Text(text = "+")
                    }
                }

                Text(
                    text = "정렬",
                    style = DiaryTheme.typography.titleLarge,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    DiaryFilterChip(
                        selected = state.sortOrder == GoldenHolidaySortOrder.LONGEST_FIRST,
                        onClick = { state.updateSortOrder(GoldenHolidaySortOrder.LONGEST_FIRST) },
                        label = { Text(text = "연휴가 긴 순") },
                    )

                    DiaryFilterChip(
                        selected = state.sortOrder == GoldenHolidaySortOrder.START_DATE,
                        onClick = { state.updateSortOrder(GoldenHolidaySortOrder.START_DATE) },
                        label = { Text(text = "시작 날짜순") },
                    )
                }
            }
        }
    }
}
