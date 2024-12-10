package io.github.taetae98coding.diary.feature.buddy.home

import androidx.compose.material3.IconButton
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldValue
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowWidthSizeClass
import io.github.taetae98coding.diary.core.compose.adaptive.isDetailVisible
import io.github.taetae98coding.diary.core.compose.adaptive.isListVisible
import io.github.taetae98coding.diary.core.compose.button.FloatingAddButton
import io.github.taetae98coding.diary.core.compose.topbar.TopBarTitle
import io.github.taetae98coding.diary.core.design.system.icon.NavigateUpIcon
import io.github.taetae98coding.diary.feature.buddy.add.BuddyAddViewModel
import io.github.taetae98coding.diary.feature.buddy.add.rememberBuddyDetailScreenAddState
import io.github.taetae98coding.diary.feature.buddy.detail.BuddyDetailScreen
import io.github.taetae98coding.diary.feature.buddy.detail.BuddyDetailScreenState
import io.github.taetae98coding.diary.feature.buddy.detail.rememberBuddyDetailScreenDetailState
import io.github.taetae98coding.diary.feature.buddy.list.BuddyListScreen
import io.github.taetae98coding.diary.feature.buddy.list.BuddyListViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun BuddyHomeRoute(
    navigateToBuddyCalendar: (String) -> Unit,
    onScaffoldValueChange: (ThreePaneScaffoldValue) -> Unit,
    modifier: Modifier = Modifier,
    listViewModel: BuddyListViewModel = koinViewModel(),
    addViewModel: BuddyAddViewModel = koinViewModel(),
) {
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val navigator = rememberListDetailPaneScaffoldNavigator<String?>(scaffoldDirective = calculatePaneScaffoldDirective(windowAdaptiveInfo))

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective.copy(defaultPanePreferredWidth = 500.dp),
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                val isFloatingVisible by remember {
                    derivedStateOf {
                        if (navigator.isDetailVisible()) {
                            navigator.currentDestination?.content != null
                        } else {
                            true
                        }
                    }
                }
                val groupList by listViewModel.groupList.collectAsStateWithLifecycle()

                BuddyListScreen(
                    listProvider = { groupList },
                    onGroup = { navigator.navigateTo(ThreePaneScaffoldRole.Primary, it) },
                    floatingActionButton = {
                        if (isFloatingVisible) {
                            FloatingAddButton(
                                onClick = { navigator.navigateTo(ThreePaneScaffoldRole.Primary, null) },
                            )
                        }
                    },
                )
            }
        },
        detailPane = {
            AnimatedPane {
                val isAdd by remember { derivedStateOf { navigator.currentDestination?.content == null } }
                val isNavigateUpVisible by remember(windowAdaptiveInfo) {
                    derivedStateOf {
                        if (windowAdaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
                            !navigator.isListVisible()
                        } else {
                            true
                        }
                    }
                }
                val state = if (isAdd) {
                    rememberBuddyDetailScreenAddState()
                } else {
                    rememberBuddyDetailScreenDetailState(
                        onCalendar = {
                            navigator.currentDestination?.content?.let(navigateToBuddyCalendar)
                        },
                    )
                }
                val uiState by addViewModel.uiState.collectAsStateWithLifecycle()
                val buddyUiState by addViewModel.buddyUiState.collectAsStateWithLifecycle()
                val buddyBottomSheetState by addViewModel.buddyBottomSheetUiState.collectAsStateWithLifecycle()

                BuddyDetailScreen(
                    state = state,
                    uiStateProvider = { uiState },
                    buddyUiStateProvider = { buddyUiState },
                    buddyBottomSheetUiStateProvider = { buddyBottomSheetState },
                    topBarTitle = {
                        if (isAdd) {
                            TopBarTitle(text = "그룹 추가")
                        } else {
                            TopBarTitle(text = "그룹")
                        }
                    },
                    navigationIcon = {
                        if (isNavigateUpVisible) {
                            IconButton(onClick = navigator::navigateBack) {
                                NavigateUpIcon()
                            }
                        }
                    },
                    floatingActionButton = {
                        if (isAdd) {
                            val isProgress by remember { derivedStateOf { uiState.isProgress } }

                            FloatingAddButton(
                                onClick = { addViewModel.add(state.detail) },
                                progressProvider = { isProgress },
                            )
                        }
                    }
                )

                FetchAccount(
                    addViewModel = addViewModel,
                    state = state,
                )
            }
        },
        modifier = modifier,
    )

    LaunchedScaffoldValue(
        navigator = navigator,
        onScaffoldValueChange = onScaffoldValueChange,
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun LaunchedScaffoldValue(
    navigator: ThreePaneScaffoldNavigator<String?>,
    onScaffoldValueChange: (ThreePaneScaffoldValue) -> Unit,
) {
    LaunchedEffect(navigator.scaffoldValue) {
        onScaffoldValueChange(navigator.scaffoldValue)
    }
}

@Composable
private fun FetchAccount(
    addViewModel: BuddyAddViewModel,
    state: BuddyDetailScreenState,
) {
    LaunchedEffect(state.buddyBottomSheetState.email) {
        addViewModel.fetch(state.buddyBottomSheetState.email)
    }
}
