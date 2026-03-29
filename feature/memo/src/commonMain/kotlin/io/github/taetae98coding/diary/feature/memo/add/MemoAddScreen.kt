package io.github.taetae98coding.diary.feature.memo.add

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.core.navigation.MemoAddNavKey
import io.github.taetae98coding.diary.presenter.memo.api.MemoAddStateHolder
import io.github.taetae98coding.diary.presenter.memo.compose.add.MemoAddScaffold
import io.github.taetae98coding.diary.presenter.memo.compose.add.rememberMemoAddScaffoldState
import kotlin.uuid.Uuid

@Composable
internal fun MemoAddScreen(
    navKey: MemoAddNavKey,
    navigateUp: () -> Unit,
    navigateToMemoAddTag: () -> Unit,
    navigateToTagDetail: (Uuid) -> Unit,
    stateHolder: MemoAddStateHolder,
    modifier: Modifier = Modifier,
) {
    val localDateRange = navKey.localDateRange

    MemoAddScaffold(
        stateHolder = stateHolder,
        modifier = modifier,
        state = rememberMemoAddScaffoldState(initialLocalDateRange = localDateRange),
        onNavigateUp = navigateUp,
        onTagCard = navigateToMemoAddTag,
        onTag = navigateToTagDetail,
    )
}
