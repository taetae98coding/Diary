package io.github.taetae98coding.diary.feature.buddy.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.core.compose.back.KBackHandler
import io.github.taetae98coding.diary.core.compose.navigation.ReverseModalDrawerSheet
import io.github.taetae98coding.diary.core.compose.navigation.ReverseModalNavigationDrawer
import io.github.taetae98coding.diary.core.design.system.diary.component.DiaryComponent
import io.github.taetae98coding.diary.core.design.system.emoji.Emoji
import io.github.taetae98coding.diary.core.design.system.icon.CalendarIcon
import io.github.taetae98coding.diary.core.design.system.icon.MemoIcon
import io.github.taetae98coding.diary.core.design.system.icon.TagIcon
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.core.model.buddy.Buddy
import io.github.taetae98coding.diary.feature.buddy.common.BuddyBottomSheetUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BuddyDetailScreen(
    state: BuddyDetailScreenState,
    uiStateProvider: () -> BuddyDetailScreenUiState,
    buddyUiStateProvider: () -> List<Buddy>?,
    buddyBottomSheetUiStateProvider: () -> BuddyBottomSheetUiState,
    modifier: Modifier = Modifier,
    topBarTitle: @Composable () -> Unit = {},
    navigationIcon: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
) {
    if (state is BuddyDetailScreenState.Detail) {
        val coroutineScope = rememberCoroutineScope()

        ReverseModalNavigationDrawer(
            modifier = modifier,
            drawerContent = {
                ReverseModalDrawerSheet {
                    NavigationDrawerItem(
                        label = { Text("메모") },
                        selected = false,
                        onClick = {},
                        icon = { MemoIcon() },
                    )
                    NavigationDrawerItem(
                        label = { Text("태그") },
                        selected = false,
                        onClick = {},
                        icon = { TagIcon() },
                    )
                    NavigationDrawerItem(
                        label = { Text("캘린더") },
                        selected = false,
                        onClick = state.onCalendar,
                        icon = { CalendarIcon() },
                    )
                }
            },
            drawerState = state.drawerState,
        ) {
            ScreenScaffold(
                state = state,
                uiStateProvider = uiStateProvider,
                buddyUiStateProvider = buddyUiStateProvider,
                buddyBottomSheetUiStateProvider = buddyBottomSheetUiStateProvider,
                topBarTitle = topBarTitle,
                navigationIcon = navigationIcon,
                floatingActionButton = floatingActionButton,
            )
        }

        KBackHandler(isEnabled = state.drawerState.isOpen) {
            coroutineScope.launch { state.drawerState.close() }
        }
    } else {
        ScreenScaffold(
            state = state,
            uiStateProvider = uiStateProvider,
            buddyUiStateProvider = buddyUiStateProvider,
            buddyBottomSheetUiStateProvider = buddyBottomSheetUiStateProvider,
            modifier = modifier,
            topBarTitle = topBarTitle,
            navigationIcon = navigationIcon,
            floatingActionButton = floatingActionButton,
        )
    }

    Message(
        state = state,
        uiStateProvider = uiStateProvider,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenScaffold(
    state: BuddyDetailScreenState,
    uiStateProvider: () -> BuddyDetailScreenUiState,
    buddyUiStateProvider: () -> List<Buddy>?,
    buddyBottomSheetUiStateProvider: () -> BuddyBottomSheetUiState,
    modifier: Modifier = Modifier,
    topBarTitle: @Composable () -> Unit = {},
    navigationIcon: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
) {
    Scaffold(
        topBar = {
            val coroutineScope = rememberCoroutineScope()

            TopAppBar(
                title = topBarTitle,
                navigationIcon = navigationIcon,
                actions = {
                    if (state is BuddyDetailScreenState.Detail) {
                        IconButton(
                            onClick = { coroutineScope.launch { state.drawerState.open() } },
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Menu,
                                contentDescription = null,
                            )
                        }
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = state.hostState) },
        floatingActionButton = floatingActionButton,
    ) {
        Content(
            state = state,
            buddyUiStateProvider = buddyUiStateProvider,
            buddyBottomSheetUiStateProvider = buddyBottomSheetUiStateProvider,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(DiaryTheme.dimen.screenPaddingValues),
        )
    }
}

@Composable
private fun Message(
    state: BuddyDetailScreenState,
    uiStateProvider: () -> BuddyDetailScreenUiState,
) {
    val uiState = uiStateProvider()

    LaunchedEffect(
        uiState.isAdd,
        uiState.isExit,
        uiState.isUpdate,
        uiState.isTitleBlankError,
        uiState.isNetworkError,
        uiState.isUnknownError,
    ) {
        if (!uiState.hasMessage) return@LaunchedEffect

        when {
            uiState.isAdd -> {
                state.showMessage("그룹 추가 ${Emoji.congratulate.random()}")
                state.clearInput()
                state.requestTitleFocus()
            }

            uiState.isExit -> {
//                if (state is TagDetailScreenState.Detail) {
//                    state.onDelete()
//                }
            }

            uiState.isUpdate -> {
//                if (state is TagDetailScreenState.Detail) {
//                    state.onUpdate()
//                }
            }

            uiState.isTitleBlankError -> {
                state.showMessage("제목을 입력해 주세요 ${Emoji.check.random()}")
                state.requestTitleFocus()
                state.titleError()
            }

            uiState.isNetworkError -> {
                state.showMessage("네트워크 상태를 확인해 주세요 ${Emoji.error.random()}")
            }

            uiState.isUnknownError -> state.showMessage("알 수 없는 에러가 발생했어요 잠시 후 다시 시도해 주세요 ${Emoji.error.random()}")
        }

        uiState.onMessageShow()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    state: BuddyDetailScreenState,
    buddyUiStateProvider: () -> List<Buddy>?,
    buddyBottomSheetUiStateProvider: () -> BuddyBottomSheetUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace),
    ) {
        DiaryComponent(
            state = state.componentState,
        )

        BuddyGroup(
            state = state.buddyBottomSheetState,
            buddyUiStateProvider = buddyUiStateProvider,
            bottomSheetUiState = buddyBottomSheetUiStateProvider,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
