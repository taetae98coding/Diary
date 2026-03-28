package io.github.taetae98coding.diary.presenter.tag.compose.add

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.graphics.toArgb
import io.github.taetae98coding.diary.compose.core.card.ColorCardState
import io.github.taetae98coding.diary.compose.core.card.DescriptionCardState
import io.github.taetae98coding.diary.compose.core.card.TitleCardState
import io.github.taetae98coding.diary.compose.core.card.rememberColorCardState
import io.github.taetae98coding.diary.compose.core.card.rememberDescriptionCardState
import io.github.taetae98coding.diary.compose.core.card.rememberTitleCardState
import io.github.taetae98coding.diary.core.model.tag.TagDetail

public class TagAddScaffoldState(
    public val titleCardState: TitleCardState,
    public val descriptionCardState: DescriptionCardState,
    public val colorCardState: ColorCardState,
) {
    public val hostState: SnackbarHostState = SnackbarHostState()

    public val detail: TagDetail
        get() = TagDetail(
            title = titleCardState.textFieldState.text.toString(),
            description = descriptionCardState.textFieldState.text.toString(),
            color = colorCardState.value.toArgb(),
        )
}

@Composable
public fun rememberTagAddScaffoldState(): TagAddScaffoldState {
    val titleCardState = rememberTitleCardState()
    val descriptionCardState = rememberDescriptionCardState()
    val colorCardState = rememberColorCardState()

    return retain(titleCardState, descriptionCardState, colorCardState) {
        TagAddScaffoldState(
            titleCardState = titleCardState,
            descriptionCardState = descriptionCardState,
            colorCardState = colorCardState,
        )
    }
}
