package io.github.taetae98coding.diary.feature.tag.add

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
internal fun TagAddScreen(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TagAddViewModel = koinViewModel(),
) {
    val isInProgress by viewModel.isInProgress.collectAsStateWithLifecycle()
    val state = rememberTagAddScaffoldState()

    TagAddScaffold(
        state = state,
        isInProgressProvider = { isInProgress },
        onNavigateUp = navigateUp,
        onAdd = { viewModel.add(state.detail) },
        modifier = modifier,
    )

    TitleFocusEffect(state = state)

    TagAddEffectHandler(
        viewModel = viewModel,
        state = state,
    )
}

@Composable
private fun TitleFocusEffect(state: TagAddScaffoldState) {
    LifecycleResumeEffect(state) {
        state.titleCardState.focusRequester.requestFocus()
        onPauseOrDispose { }
    }
}

@Composable
private fun TagAddEffectHandler(
    viewModel: TagAddViewModel,
    state: TagAddScaffoldState,
) {
    val coroutineScope = rememberCoroutineScope()
    val effect by viewModel.effect.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel, state, effect) {
        when (effect) {
            TagAddEffect.AddFinish -> {
                coroutineScope.launch { state.hostState.showImmediate("태그가 추가되었습니다") }
                coroutineScope.launch { state.reset() }
                state.titleCardState.focusRequester.requestFocus()
                viewModel.consumeEffect()
            }

            TagAddEffect.TitleBlank -> {
                coroutineScope.launch { state.hostState.showImmediate("제목을 입력해주세요") }
                state.titleCardState.focusRequester.requestFocus()
                viewModel.consumeEffect()
            }

            TagAddEffect.UnknownError -> {
                coroutineScope.launch { state.hostState.showImmediate("네트워크 상태를 확인하세요") }
                viewModel.consumeEffect()
            }

            TagAddEffect.None -> Unit
        }
    }
}
