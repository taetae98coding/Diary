package io.github.taetae98coding.diary.feature.routine.detail

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
internal fun RoutineDetailScreen(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RoutineDetailViewModel = koinViewModel(),
) {
    val routine by viewModel.routine.collectAsStateWithLifecycle()
    val updateInProgress by viewModel.updateInProgress.collectAsStateWithLifecycle()
    val finishUiState by viewModel.finishUiState.collectAsStateWithLifecycle()
    val deleteUiState by viewModel.deleteUiState.collectAsStateWithLifecycle()

    val state = rememberRoutineDetailScaffoldState(routineProvider = { routine })

    RoutineDetailScaffold(
        state = state,
        detailProvider = { routine?.detail },
        finishUiStateProvider = { finishUiState },
        deleteUiStateProvider = { deleteUiState },
        updateInProgressProvider = { updateInProgress },
        rRulesProvider = { routine?.rRules.orEmpty() },
        onNavigateUp = navigateUp,
        onUpdate = { viewModel.update(state.detail) },
        onAddRRule = viewModel::addRRule,
        onRemoveRRule = viewModel::removeRRule,
        onFinish = viewModel::finish,
        onRestart = viewModel::restart,
        onDelete = viewModel::delete,
        modifier = modifier,
    )

    RoutineDetailEffectHandler(
        viewModel = viewModel,
        state = state,
        navigateUp = navigateUp,
    )
}

@Composable
private fun RoutineDetailEffectHandler(
    viewModel: RoutineDetailViewModel,
    state: RoutineDetailScaffoldState,
    navigateUp: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val effect by viewModel.effect.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel, state, effect) {
        when (effect) {
            RoutineDetailEffect.UpdateFinish -> {
                coroutineScope.launch { state.hostState.showImmediate("루틴이 수정되었습니다") }
                viewModel.consumeEffect()
            }

            RoutineDetailEffect.DeleteFinish -> {
                navigateUp()
                viewModel.consumeEffect()
            }

            RoutineDetailEffect.UnknownError -> {
                coroutineScope.launch { state.hostState.showImmediate("네트워크 상태를 확인하세요") }
                viewModel.consumeEffect()
            }

            RoutineDetailEffect.None -> Unit
        }
    }
}
