package io.github.taetae98coding.diary.core.compose.tag

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.core.compose.adaptive.isDetailVisible
import io.github.taetae98coding.diary.core.compose.adaptive.isListVisible
import io.github.taetae98coding.diary.core.compose.back.KBackHandler
import io.github.taetae98coding.diary.core.compose.tag.add.rememberTagDetailScaffoldAddState
import io.github.taetae98coding.diary.core.compose.tag.detail.TagDetailScaffold
import io.github.taetae98coding.diary.core.compose.tag.detail.TagDetailScaffoldActions
import io.github.taetae98coding.diary.core.compose.tag.detail.TagDetailScaffoldActionsUiState
import io.github.taetae98coding.diary.core.compose.tag.detail.TagDetailScaffoldFloatingButton
import io.github.taetae98coding.diary.core.compose.tag.detail.TagDetailScaffoldNavigationIcon
import io.github.taetae98coding.diary.core.compose.tag.detail.TagDetailScaffoldState
import io.github.taetae98coding.diary.core.compose.tag.detail.TagDetailScaffoldUiState
import io.github.taetae98coding.diary.core.compose.tag.detail.rememberTagDetailScaffoldDetailState
import io.github.taetae98coding.diary.core.compose.tag.list.TagListScaffold
import io.github.taetae98coding.diary.core.compose.tag.list.TagListScaffoldFloatingButton
import io.github.taetae98coding.diary.core.compose.tag.list.TagListScaffoldUiState
import io.github.taetae98coding.diary.core.compose.tag.list.TagListUiState
import io.github.taetae98coding.diary.core.compose.tag.list.rememberTagListScaffoldState
import io.github.taetae98coding.diary.core.compose.tag.memo.TagMemoListUiState
import io.github.taetae98coding.diary.core.compose.tag.memo.TagMemoNavigateIcon
import io.github.taetae98coding.diary.core.compose.tag.memo.TagMemoScaffold
import io.github.taetae98coding.diary.core.compose.tag.memo.TagMemoScaffoldUiState
import io.github.taetae98coding.diary.core.compose.tag.memo.rememberTagMemoScaffoldState
import io.github.taetae98coding.diary.core.model.tag.TagDetail

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
public fun TagListMultiPaneScaffold(
	navigateToMemoAdd: (String) -> Unit,
	navigateToMemoDetail: (String) -> Unit,
	navigator: ThreePaneScaffoldNavigator<String?>,
	listScaffoldUiStateProvider: () -> TagListScaffoldUiState,
	listUiStateProvider: () -> TagListUiState,
	addUiStateProvider: () -> TagDetailScaffoldUiState.Add,
	detailProvider: () -> TagDetail?,
	detailUiStateProvider: () -> TagDetailScaffoldUiState.Detail,
	detailActionsUiStateProvider: () -> TagDetailScaffoldActionsUiState,
	tagMemoListUiStateProvider: () -> TagMemoListUiState,
	tagMemoUiStateProvider: () -> TagMemoScaffoldUiState,
	modifier: Modifier = Modifier,
) {
	var tagListDetailNavigate by remember { mutableStateOf<TagListDetailNavigate>(TagListDetailNavigate.None) }

	ListDetailPaneScaffold(
		directive = navigator.scaffoldDirective.copy(defaultPanePreferredWidth = 500.dp),
		value = navigator.scaffoldValue,
		listPane = {
			AnimatedPane {
				TagListScaffold(
					state = rememberTagListScaffoldState(
						navigateToTag = {
							if (navigator.isDetailVisible() && navigator.currentDestination?.content != null) {
								tagListDetailNavigate = TagListDetailNavigate.Tag(it)
							} else {
								navigator.navigateTo(ThreePaneScaffoldRole.Primary, it)
							}
						},
					),
					uiStateProvider = listScaffoldUiStateProvider,
					listUiStateProvider = listUiStateProvider,
					floatingButtonProvider = {
						val isVisible = if (navigator.isDetailVisible()) {
							navigator.currentDestination?.content != null
						} else {
							true
						}

						if (isVisible) {
							TagListScaffoldFloatingButton.Add {
								if (navigator.isDetailVisible() && navigator.currentDestination?.content != null) {
									tagListDetailNavigate = TagListDetailNavigate.Add
								} else {
									navigator.navigateTo(ThreePaneScaffoldRole.Primary)
								}
							}
						} else {
							TagListScaffoldFloatingButton.None
						}
					},
				)
			}
		},
		detailPane = {
			AnimatedPane {
				val state = if (navigator.currentDestination?.content == null) {
					rememberTagDetailScaffoldAddState()
				} else {
					rememberTagDetailScaffoldDetailState(
						onUpdate = {
							when (val navigate = tagListDetailNavigate) {
								is TagListDetailNavigate.Add -> {
									navigator.navigateTo(ThreePaneScaffoldRole.Primary)
									tagListDetailNavigate = TagListDetailNavigate.None
								}

								is TagListDetailNavigate.Tag -> {
									navigator.navigateTo(ThreePaneScaffoldRole.Primary, navigate.tagId)
									tagListDetailNavigate = TagListDetailNavigate.None
								}

								is TagListDetailNavigate.None -> {
									navigator.navigateBack()
								}
							}
						},
						onDelete = navigator::navigateBack,
						navigateToMemo = { navigator.currentDestination?.content?.let { navigator.navigateTo(ThreePaneScaffoldRole.Tertiary, it) } },
						detailProvider = detailProvider,
					)
				}

				TagDetailScaffold(
					state = state,
					uiStateProvider = {
						if (navigator.currentDestination?.content == null) {
							addUiStateProvider()
						} else {
							detailUiStateProvider()
						}
					},
					titleProvider = {
						if (navigator.currentDestination?.content == null) {
							"태그 추가"
						} else {
							detailProvider()?.title
						}
					},
					navigationIconProvider = {
						if (!navigator.isListVisible()) {
							TagDetailScaffoldNavigationIcon.NavigateUp {
								if (navigator.currentDestination?.content == null) {
									navigator.navigateBack()
								} else {
									detailUiStateProvider().update(state.tagDetail)
								}
							}
						} else {
							TagDetailScaffoldNavigationIcon.None
						}
					},
					actionsProvider = {
						if (navigator.currentDestination?.content == null) {
							TagDetailScaffoldActions.None
						} else {
							val actionsUiState = detailActionsUiStateProvider()

							TagDetailScaffoldActions.FinishAndDelete(
								isFinish = actionsUiState.isFinish,
								finish = actionsUiState.finish,
								restart = actionsUiState.restart,
								delete = actionsUiState.delete,
							)
						}
					},
					floatingButtonProvider = {
						if (navigator.currentDestination?.content == null) {
							val uiState = addUiStateProvider()

							TagDetailScaffoldFloatingButton.Add(
								add = { uiState.add(state.tagDetail) },
								isInProgress = uiState.isAddInProgress,
							)
						} else {
							TagDetailScaffoldFloatingButton.None
						}
					},
				)

				HandleNavigate(
					state = state,
					navigateProvider = { tagListDetailNavigate },
					detailUiStateProvider = detailUiStateProvider,
				)
			}
		},
		modifier = modifier,
		extraPane = {
			AnimatedPane {
				TagMemoScaffold(
					state = rememberTagMemoScaffoldState(),
					uiStateProvider = tagMemoUiStateProvider,
					listUiStateProvider = tagMemoListUiStateProvider,
					onAdd = { navigator.currentDestination?.content?.let(navigateToMemoAdd) },
					onMemo = navigateToMemoDetail,
					navigateIconProvider = {
						if (navigator.isDetailVisible()) {
							TagMemoNavigateIcon.None
						} else {
							TagMemoNavigateIcon.NavigateUp(navigator::navigateBack)
						}
					},
				)
			}
		},
	)

	NavigateUp(navigator = navigator)
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun NavigateUp(
	navigator: ThreePaneScaffoldNavigator<*>,
) {
	KBackHandler(
		isEnabled = navigator.canNavigateBack(),
		onBack = navigator::navigateBack,
	)
}

@Composable
private fun HandleNavigate(
	state: TagDetailScaffoldState,
	navigateProvider: () -> TagListDetailNavigate,
	detailUiStateProvider: () -> TagDetailScaffoldUiState.Detail,
) {
	val navigate = navigateProvider()

	LaunchedEffect(navigate) {
		when (navigate) {
			is TagListDetailNavigate.Add, is TagListDetailNavigate.Tag -> {
				detailUiStateProvider().update(state.tagDetail)
			}

			is TagListDetailNavigate.None -> Unit
		}
	}
}
