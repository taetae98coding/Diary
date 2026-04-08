package io.github.taetae98coding.diary.feature.tag.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.compose.core.snackbar.showImmediate
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun TagDetailScreen(
    navigateUp: () -> Unit,
    navigateToTagMemo: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TagDetailViewModel = koinViewModel(),
) {
    val updateInProgress by viewModel.updateInProgress.collectAsStateWithLifecycle()
    val finishUiState by viewModel.finishUiState.collectAsStateWithLifecycle()
    val deleteUiState by viewModel.deleteUiState.collectAsStateWithLifecycle()
    val tag by viewModel.tag.collectAsStateWithLifecycle()
    val state = rememberTagDetailScaffoldState(tagProvider = { tag })

    TagDetailScaffold(
        state = state,
        detailProvider = { tag?.detail },
        finishUiStateProvider = { finishUiState },
        deleteUiStateProvider = { deleteUiState },
        updateInProgressProvider = { updateInProgress },
        onNavigateUp = navigateUp,
        onNavigateToTagMemo = navigateToTagMemo,
        onUpdate = { viewModel.update(state.detail) },
        onFinish = viewModel::finish,
        onRestart = viewModel::restart,
        onDelete = viewModel::delete,
        modifier = modifier,
    )

    TagDetailEffectHandler(
        viewModel = viewModel,
        state = state,
        onNavigateUp = navigateUp,
    )
}

@Composable
private fun TagDetailEffectHandler(
    viewModel: TagDetailViewModel,
    state: TagDetailScaffoldState,
    onNavigateUp: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val effect by viewModel.effect.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel, state, effect) {
        when (effect) {
            TagDetailEffect.UpdateFinish -> {
                coroutineScope.launch { state.hostState.showImmediate("태그가 수정되었습니다") }
                viewModel.consumeEffect()
            }

            TagDetailEffect.DeleteFinish -> {
                onNavigateUp()
                viewModel.consumeEffect()
            }

            TagDetailEffect.UnknownError -> {
                coroutineScope.launch { state.hostState.showImmediate("네트워크 상태를 확인하세요") }
                viewModel.consumeEffect()
            }

            TagDetailEffect.None -> Unit
        }
    }
}
