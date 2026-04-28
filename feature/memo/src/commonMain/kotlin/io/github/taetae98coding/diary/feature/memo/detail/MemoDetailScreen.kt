package io.github.taetae98coding.diary.feature.memo.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.compose.core.snackbar.showImmediate
import kotlin.uuid.Uuid
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun MemoDetailScreen(
    navigateUp: () -> Unit,
    navigateToMemoDetailTag: () -> Unit,
    navigateToTagDetail: (Uuid) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MemoDetailViewModel = koinViewModel(),
) {
    val updateInProgress by viewModel.updateInProgress.collectAsStateWithLifecycle()
    val finishUiState by viewModel.finishUiState.collectAsStateWithLifecycle()
    val copyUiState by viewModel.copyUiState.collectAsStateWithLifecycle()
    val deleteUiState by viewModel.deleteUiState.collectAsStateWithLifecycle()
    val memo by viewModel.memo.collectAsStateWithLifecycle()
    val tagCardUiState by viewModel.tagCardUiState.collectAsStateWithLifecycle()
    val state = rememberMemoDetailScaffoldState(memoProvider = { memo })

    MemoDetailScaffold(
        state = state,
        detailProvider = { memo?.detail },
        finishUiStateProvider = { finishUiState },
        copyUiStateProvider = { copyUiState },
        deleteUiStateProvider = { deleteUiState },
        updateInProgressProvider = { updateInProgress },
        tagCardUiStateProvider = { tagCardUiState },
        onNavigateUp = navigateUp,
        onUpdate = { viewModel.update(state.detail) },
        onFinish = viewModel::finish,
        onRestart = viewModel::restart,
        onCopy = viewModel::copy,
        onDelete = viewModel::delete,
        onTagCard = navigateToMemoDetailTag,
        onTag = navigateToTagDetail,
        modifier = modifier,
    )

    MemoDetailEffectHandler(
        viewModel = viewModel,
        state = state,
        onNavigateUp = navigateUp,
    )
}

@Composable
private fun MemoDetailEffectHandler(
    viewModel: MemoDetailViewModel,
    state: MemoDetailScaffoldState,
    onNavigateUp: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val effect by viewModel.effect.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel, state, effect) {
        when (effect) {
            MemoDetailEffect.UpdateFinish -> {
                coroutineScope.launch { state.hostState.showImmediate("메모가 수정되었습니다") }
                viewModel.consumeEffect()
            }

            MemoDetailEffect.CopyFinish -> {
                coroutineScope.launch { state.hostState.showImmediate("메모가 복사되었습니다") }
                viewModel.consumeEffect()
            }

            MemoDetailEffect.DeleteFinish -> {
                onNavigateUp()
                viewModel.consumeEffect()
            }

            MemoDetailEffect.UnknownError -> {
                coroutineScope.launch { state.hostState.showImmediate("네트워크 상태를 확인하세요") }
                viewModel.consumeEffect()
            }

            MemoDetailEffect.None -> Unit
        }
    }
}
