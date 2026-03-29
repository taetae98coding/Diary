package io.github.taetae98coding.diary.feature.tag.add

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.presenter.tag.api.TagAddStateHolder
import io.github.taetae98coding.diary.presenter.tag.compose.add.TagAddScaffold

@Composable
internal fun TagAddScreen(
    navigateUp: () -> Unit,
    stateHolder: TagAddStateHolder,
    modifier: Modifier = Modifier,
) {
    TagAddScaffold(
        stateHolder = stateHolder,
        modifier = modifier,
        onNavigateUp = navigateUp,
    )
}
