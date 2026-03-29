package io.github.taetae98coding.diary.feature.tag.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.presenter.tag.api.TagListStateHolder
import io.github.taetae98coding.diary.presenter.tag.compose.list.TagListScaffold
import kotlin.uuid.Uuid
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun TagHomeScreen(
    navigateToTagAdd: () -> Unit,
    navigateToTagDetail: (Uuid) -> Unit,
    stateHolder: TagListStateHolder,
    modifier: Modifier = Modifier,
    viewModel: TagHomeViewModel = koinViewModel(),
) {
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()

    TagListScaffold(
        stateHolder = stateHolder,
        modifier = modifier,
        isFetchingProvider = { isSyncing },
        onFetch = viewModel::sync,
        onNavigateToAdd = navigateToTagAdd,
        onNavigateToDetail = navigateToTagDetail,
    )
}
