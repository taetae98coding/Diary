package io.github.taetae98coding.diary.feature.memo.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.taetae98coding.diary.compose.core.snackbar.showImmediate
import io.github.taetae98coding.diary.core.navigation.MemoAddNavKey
import kotlin.uuid.Uuid
import kotlinx.coroutines.launch

@Composable
internal fun MemoAddScreen(
    navKey: MemoAddNavKey,
    navigateUp: () -> Unit,
    navigateToMemoAddTag: () -> Unit,
    navigateToTagDetail: (Uuid) -> Unit,
    stateHolder: MemoAddStateHolder,
    modifier: Modifier = Modifier,
) {
    val isInProgress by stateHolder.isInProgress.collectAsStateWithLifecycle()
    val tagCardUiState by stateHolder.tagCardUiState.collectAsStateWithLifecycle()
    val localDateRange = navKey.localDateRange
    val state = rememberMemoAddScaffoldState(initialLocalDateRange = localDateRange)

    MemoAddScaffold(
        modifier = modifier,
        state = state,
        tagCardUiStateProvider = { tagCardUiState },
        isInProgressProvider = { isInProgress },
        onNavigateUp = navigateUp,
        onTagCard = navigateToMemoAddTag,
        onTag = navigateToTagDetail,
        onAdd = { stateHolder.add(state.detail) },
    )

    TitleFocusEffect(state = state)

    MemoAddEffectHandler(
        stateHolder = stateHolder,
        state = state,
    )
}

@Composable
private fun TitleFocusEffect(state: MemoAddScaffoldState) {
    LifecycleResumeEffect(state) {
        state.titleCardState.focusRequester.requestFocus()
        onPauseOrDispose { }
    }
}

@Composable
private fun MemoAddEffectHandler(
    stateHolder: MemoAddStateHolder,
    state: MemoAddScaffoldState,
) {
    val coroutineScope = rememberCoroutineScope()
    val effect by stateHolder.effect.collectAsStateWithLifecycle()

    LaunchedEffect(stateHolder, state, effect) {
        when (effect) {
            MemoAddEffect.AddFinish -> {
                coroutineScope.launch { state.hostState.showImmediate("메모가 추가되었습니다") }
                coroutineScope.launch { state.reset() }
                state.titleCardState.focusRequester.requestFocus()
                stateHolder.consumeEffect()
            }

            MemoAddEffect.TitleBlank -> {
                coroutineScope.launch { state.hostState.showImmediate("제목을 입력해주세요") }
                state.titleCardState.focusRequester.requestFocus()
                stateHolder.consumeEffect()
            }

            MemoAddEffect.UnknownError -> {
                coroutineScope.launch { state.hostState.showImmediate("네트워크 상태를 확인하세요") }
                stateHolder.consumeEffect()
            }

            MemoAddEffect.None -> Unit
        }
    }
}
