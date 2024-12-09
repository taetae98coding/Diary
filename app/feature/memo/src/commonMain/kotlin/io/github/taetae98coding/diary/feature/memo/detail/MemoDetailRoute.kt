package io.github.taetae98coding.diary.feature.memo.detail

import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.core.compose.memo.MemoListDetailPaneScaffold
import io.github.taetae98coding.diary.core.compose.memo.detail.rememberMemoDetailScaffoldDetailState
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun MemoDetailRoute(
    navigateUp: () -> Unit,
    navigateToTagAdd: () -> Unit,
    navigateToTagDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
    detailViewModel: MemoDetailViewModel = koinViewModel(),
    detailTagViewModel: MemoDetailTagViewModel = koinViewModel(),
) {
    val detail by detailViewModel.detail.collectAsStateWithLifecycle()
    val state = rememberMemoDetailScaffoldDetailState(
        onDelete = navigateUp,
        onUpdate = navigateUp,
        detailProvider = { detail },
    )
    val uiState by detailViewModel.uiState.collectAsStateWithLifecycle()
    val primaryTagList by detailTagViewModel.primaryTagList.collectAsStateWithLifecycle()
    val tagList by detailTagViewModel.tagList.collectAsStateWithLifecycle()

    MemoListDetailPaneScaffold(
        onNavigateUp = navigateUp,
        onDetailTag = navigateToTagDetail,
        detailScaffoldStateProvider = { state },
        detailUiStateProvider = { uiState },
        detailTagListProvider = { primaryTagList },
        detailTitle = { Text(text = detail?.title.orEmpty()) },
        onTagAdd = navigateToTagAdd,
        tagListProvider = { tagList },
    )
}
