package io.github.taetae98coding.diary.feature.routine.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.compose.core.snackbar.showImmediate
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun RoutineAddScreen(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RoutineAddViewModel = koinViewModel(),
) {
    val isInProgress by viewModel.isInProgress.collectAsStateWithLifecycle()
    val state = rememberRoutineAddScaffoldState()

    RoutineAddScaffold(
        modifier = modifier,
        state = state,
        isInProgressProvider = { isInProgress },
        onNavigateUp = navigateUp,
        onAdd = {
            viewModel.add(
                detail = state.detail,
                rRules = state.rRules.toList(),
                isCalendarVisible = state.calendarVisibilityState.isVisible,
            )
        },
    )

    TitleFocusEffect(state = state)

    RoutineAddEffectHandler(
        viewModel = viewModel,
        state = state,
    )
}

@Composable
private fun TitleFocusEffect(state: RoutineAddScaffoldState) {
    LifecycleResumeEffect(state) {
        state.titleCardState.focusRequester.requestFocus()
        onPauseOrDispose { }
    }
}

@Composable
private fun RoutineAddEffectHandler(
    viewModel: RoutineAddViewModel,
    state: RoutineAddScaffoldState,
) {
    val coroutineScope = rememberCoroutineScope()
    val effect by viewModel.effect.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel, state, effect) {
        when (effect) {
            RoutineAddEffect.AddFinish -> {
                coroutineScope.launch { state.hostState.showImmediate("루틴이 추가되었습니다") }
                coroutineScope.launch { state.reset() }
                state.titleCardState.focusRequester.requestFocus()
                viewModel.consumeEffect()
            }

            RoutineAddEffect.TitleBlank -> {
                coroutineScope.launch { state.hostState.showImmediate("제목을 입력해주세요") }
                state.titleCardState.focusRequester.requestFocus()
                viewModel.consumeEffect()
            }

            RoutineAddEffect.RRuleEmpty -> {
                coroutineScope.launch { state.hostState.showImmediate("반복 규칙을 1개 이상 추가하세요") }
                viewModel.consumeEffect()
            }

            RoutineAddEffect.UnknownError -> {
                coroutineScope.launch { state.hostState.showImmediate("네트워크 상태를 확인하세요") }
                viewModel.consumeEffect()
            }

            RoutineAddEffect.None -> Unit
        }
    }
}
