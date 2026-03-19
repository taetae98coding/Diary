package io.github.taetae98coding.diary.presenter.memo.compose.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import io.github.taetae98coding.diary.compose.core.button.AddFloatingButton
import io.github.taetae98coding.diary.compose.core.button.NavigateUpButton
import io.github.taetae98coding.diary.compose.core.card.ColorCard
import io.github.taetae98coding.diary.compose.core.card.DateRangeCard
import io.github.taetae98coding.diary.compose.core.card.DescriptionCard
import io.github.taetae98coding.diary.compose.core.card.TitleCard
import io.github.taetae98coding.diary.compose.core.modifier.focusableKeyEvent
import io.github.taetae98coding.diary.compose.core.snackbar.showImmediate
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.library.compose.ui.random
import io.github.taetae98coding.diary.presenter.memo.api.MemoAddEffect
import io.github.taetae98coding.diary.presenter.memo.api.MemoAddStateHolder
import kotlinx.coroutines.launch

@Composable
public fun MemoAddScaffold(
    stateHolder: MemoAddStateHolder,
    modifier: Modifier = Modifier,
    state: MemoAddScaffoldState = rememberMemoAddScaffoldState(),
    onNavigateUp: () -> Unit = {},
) {
    val isInProgress by stateHolder.isInProgress.collectAsStateWithLifecycle()

    MemoAddScaffold(
        state = state,
        isInProgressProvider = { isInProgress },
        onNavigateUp = onNavigateUp,
        onAdd = { stateHolder.add(state.detail) },
        modifier = modifier,
    )

    TitleFocusEffect(state = state)

    MemoAddEffectHandler(
        stateHolder = stateHolder,
        state = state,
    )
}

@Composable
internal fun MemoAddScaffold(
    state: MemoAddScaffoldState,
    isInProgressProvider: () -> Boolean,
    onNavigateUp: () -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier
            .focusableKeyEvent(autoFocus = false) { event ->
                if (event.type == KeyEventType.KeyDown && event.isMetaPressed && event.key == Key.Enter) {
                    onAdd()
                    true
                } else {
                    false
                }
            }.imePadding(),
        topBar = { TopBar(onNavigateUp = onNavigateUp) },
        floatingActionButton = {
            AddFloatingButton(
                onClick = dropUnlessResumed(block = onAdd),
                isInProgressProvider = isInProgressProvider,
            )
        },
        snackbarHost = { SnackbarHost(hostState = state.hostState) },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = DiaryTheme.dimen.screenPaddingValues,
            verticalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.screenCardSpace),
        ) {
            item {
                TitleCard(state = state.titleCardState)
            }

            item {
                DescriptionCard(state = state.descriptionCardState)
            }

            item {
                DateRangeCard(state = state.dateRangeCardState)
            }

            item {
                ColorCard(
                    state = state.colorCardState,
                    modifier = Modifier.fillParentMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun TitleFocusEffect(state: MemoAddScaffoldState) {
    LaunchedEffect(state) {
        state.titleCardState.focusRequester.requestFocus()
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
                state.titleCardState.textFieldState.clearText()
                state.descriptionCardState.textFieldState.clearText()
                state.titleCardState.focusRequester.requestFocus()
                coroutineScope.launch { state.colorCardState.updateColor(Color.random()) }
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

@Composable
private fun TopBar(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { Text(text = "메모 추가") },
        modifier = modifier,
        navigationIcon = { NavigateUpButton(onClick = dropUnlessResumed(block = onNavigateUp)) },
    )
}
