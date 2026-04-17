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
    val updateInProgress by viewModel.updateInProgress.collectAsStateWithLifecycle()
    val routine by viewModel.routine.collectAsStateWithLifecycle()
    val state = rememberRoutineDetailScaffoldState(routineProvider = { routine })

    RoutineDetailScaffold(
        modifier = modifier,
        state = state,
        routineProvider = { routine },
        updateInProgressProvider = { updateInProgress },
        onNavigateUp = navigateUp,
        onUpdate = {
            viewModel.update(
                detail = state.detail,
                rRules = state.rRules.toList(),
                rDates = state.rDates.toSet(),
                exDates = state.exDates.toSet(),
            )
        },
    )

    RoutineDetailEffectHandler(
        viewModel = viewModel,
        state = state,
    )
}

@Composable
private fun RoutineDetailEffectHandler(
    viewModel: RoutineDetailViewModel,
    state: RoutineDetailScaffoldState,
) {
    val coroutineScope = rememberCoroutineScope()
    val effect by viewModel.effect.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel, state, effect) {
        when (effect) {
            RoutineDetailEffect.UpdateFinish -> {
                coroutineScope.launch { state.hostState.showImmediate("루틴이 수정되었습니다") }
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
