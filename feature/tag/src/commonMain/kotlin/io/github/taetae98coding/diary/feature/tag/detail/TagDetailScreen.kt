package io.github.taetae98coding.diary.feature.tag.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.presenter.tag.api.TagDetailStateHolder
import io.github.taetae98coding.diary.presenter.tag.compose.detail.TagDetailScaffold

@Composable
internal fun TagDetailScreen(
    navigateUp: () -> Unit,
    stateHolder: TagDetailStateHolder,
    modifier: Modifier = Modifier,
) {
    TagDetailScaffold(
        stateHolder = stateHolder,
        modifier = modifier,
        onNavigateUp = navigateUp,
    )
}
