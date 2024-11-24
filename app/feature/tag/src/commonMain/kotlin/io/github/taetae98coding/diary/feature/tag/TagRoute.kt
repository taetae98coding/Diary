package io.github.taetae98coding.diary.feature.tag

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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowWidthSizeClass
import io.github.taetae98coding.diary.core.compose.adaptive.isDetailVisible
import io.github.taetae98coding.diary.core.compose.adaptive.isListVisible
import io.github.taetae98coding.diary.core.compose.back.KBackHandler
import io.github.taetae98coding.diary.feature.tag.add.TagAddViewModel
import io.github.taetae98coding.diary.feature.tag.add.rememberTagDetailScreenAddState
import io.github.taetae98coding.diary.feature.tag.detail.TagDetailActionButton
import io.github.taetae98coding.diary.feature.tag.detail.TagDetailFloatingButton
import io.github.taetae98coding.diary.feature.tag.detail.TagDetailNavigationButton
import io.github.taetae98coding.diary.feature.tag.detail.TagDetailScreen
import io.github.taetae98coding.diary.feature.tag.detail.TagDetailViewModel
import io.github.taetae98coding.diary.feature.tag.detail.rememberTagDetailScreenDetailState
import io.github.taetae98coding.diary.feature.tag.list.TagListFloatingButton
import io.github.taetae98coding.diary.feature.tag.list.TagListScreen
import io.github.taetae98coding.diary.feature.tag.list.TagListViewModel
import io.github.taetae98coding.diary.feature.tag.list.rememberTagListScreenState
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun TagRoute(
    onScaffoldValueChange: (ThreePaneScaffoldValue) -> Unit,
    modifier: Modifier = Modifier,
    listViewModel: TagListViewModel = koinViewModel(),
    addViewModel: TagAddViewModel = koinViewModel(),
    detailViewModel: TagDetailViewModel = koinViewModel(),
) {
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val navigator = rememberListDetailPaneScaffoldNavigator<String?>(scaffoldDirective = calculatePaneScaffoldDirective(windowAdaptiveInfo))

    var tagNavigate by remember { mutableStateOf<TagNavigate>(TagNavigate.None) }
    var detailPaneRefreshCount by remember { mutableIntStateOf(0) }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
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

                val list by listViewModel.list.collectAsStateWithLifecycle()
                val uiState by listViewModel.uiState.collectAsStateWithLifecycle()

                TagListScreen(
                    state = rememberTagListScreenState(),
                    floatingButtonProvider = {
                        if (isFloatingVisible) {
                            TagListFloatingButton.Add(
                                onAdd = {
                                    if (navigator.isDetailVisible()) {
                                        tagNavigate = TagNavigate.Add
                                    } else {
                                        navigator.navigateTo(ThreePaneScaffoldRole.Primary)
                                    }
                                },
                            )
                        } else {
                            TagListFloatingButton.None
                        }
                    },
                    listProvider = { list },
                    onTag = {
                        if (navigator.isDetailVisible() && navigator.currentDestination?.content != null) {
                            tagNavigate = TagNavigate.Tag(it)
                        } else {
                            navigator.navigateTo(ThreePaneScaffoldRole.Primary, it)
                            detailPaneRefreshCount++
                        }
                    },
                    uiStateProvider = { uiState },
                )
            }
        },
        detailPane = {
            AnimatedPane {
                val tagDetail by detailViewModel.tagDetail.collectAsStateWithLifecycle()
                val isAdd = remember(detailPaneRefreshCount) { navigator.currentDestination?.content == null }
                val state = if (isAdd) {
                    rememberTagDetailScreenAddState()
                } else {
                    rememberTagDetailScreenDetailState(
                        onUpdate = {
                            when (val navigate = tagNavigate) {
                                is TagNavigate.Add -> {
                                    navigator.navigateTo(ThreePaneScaffoldRole.Primary)
                                    detailPaneRefreshCount++
                                    tagNavigate = TagNavigate.None
                                }

                                is TagNavigate.Tag -> {
                                    navigator.navigateTo(ThreePaneScaffoldRole.Primary, navigate.tagId)
                                    tagNavigate = TagNavigate.None
                                }

                                is TagNavigate.None -> {
                                    navigator.navigateBack()
                                }
                            }
                        },
                        onDelete = {
                            navigator.navigateBack()
                            if (windowAdaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
                                detailPaneRefreshCount++
                            }
                        },
                        detailProvider = { tagDetail },
                    )
                }
                val isNavigateUpVisible = remember(windowAdaptiveInfo) {
                    if (windowAdaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
                        !navigator.isListVisible()
                    } else {
                        true
                    }
                }
                val detailActionButton by detailViewModel.actionButton.collectAsStateWithLifecycle()
                val uiState = if (isAdd) {
                    addViewModel.uiState.collectAsStateWithLifecycle()
                } else {
                    detailViewModel.uiState.collectAsStateWithLifecycle()
                }

                TagDetailScreen(
                    state = state,
                    titleProvider = {
                        if (isAdd) {
                            "태그 추가"
                        } else {
                            tagDetail?.title
                        }
                    },
                    navigateButtonProvider = {
                        if (isNavigateUpVisible) {
                            TagDetailNavigationButton.NavigateUp(
                                onNavigateUp = {
                                    if (isAdd) {
                                        navigator.navigateBack()
                                    } else {
                                        detailViewModel.update(state.tagDetail)
                                    }
                                },
                            )
                        } else {
                            TagDetailNavigationButton.None
                        }
                    },
                    actionButtonProvider = {
                        if (isAdd) {
                            TagDetailActionButton.None
                        } else {
                            detailActionButton
                        }
                    },
                    floatingButtonProvider = {
                        if (isAdd) {
                            TagDetailFloatingButton.Add(onAdd = { addViewModel.add(state.tagDetail) })
                        } else {
                            TagDetailFloatingButton.None
                        }
                    },
                    uiStateProvider = { uiState.value },
                )

                LaunchedEffect(tagNavigate) {
                    when (tagNavigate) {
                        is TagNavigate.Add -> {
                            detailViewModel.update(state.tagDetail)
                        }

                        is TagNavigate.Tag -> {
                            detailViewModel.update(state.tagDetail)
                        }

                        is TagNavigate.None -> Unit
                    }
                }
            }
        },
        modifier = modifier,
    )

    LaunchedScaffoldValue(
        navigator = navigator,
        onScaffoldValueChange = onScaffoldValueChange,
    )

    LaunchedFetch(
        navigator = navigator,
        detailViewModel = detailViewModel,
    )

    KBackHandler(
        isEnabled = navigator.canNavigateBack(),
        onBack = navigator::navigateBack,
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun LaunchedFetch(
    navigator: ThreePaneScaffoldNavigator<String?>,
    detailViewModel: TagDetailViewModel,
) {
    LaunchedEffect(navigator.currentDestination?.content, detailViewModel) {
        detailViewModel.fetch(navigator.currentDestination?.content)
    }
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
