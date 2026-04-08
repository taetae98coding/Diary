package io.github.taetae98coding.diary.feature.tag.detail

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import io.github.taetae98coding.diary.compose.core.card.ColorCardState
import io.github.taetae98coding.diary.compose.core.card.DescriptionCardState
import io.github.taetae98coding.diary.compose.core.card.TitleCardState
import io.github.taetae98coding.diary.compose.core.card.rememberColorCardState
import io.github.taetae98coding.diary.compose.core.card.rememberDescriptionCardState
import io.github.taetae98coding.diary.compose.core.card.rememberTitleCardState
import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.core.model.tag.TagDetail

internal class TagDetailScaffoldState(
    val titleCardState: TitleCardState,
    val descriptionCardState: DescriptionCardState,
    val colorCardState: ColorCardState,
) {
    val hostState: SnackbarHostState = SnackbarHostState()

    val detail: TagDetail
        get() = TagDetail(
            title = titleCardState.textFieldState.text.toString(),
            description = descriptionCardState.textFieldState.text.toString(),
            color = colorCardState.value.toArgb(),
        )
}

@Composable
internal fun rememberTagDetailScaffoldState(tagProvider: () -> Tag?): TagDetailScaffoldState {
    val tag = tagProvider()
    val detail = tag?.detail

    val titleCardState = rememberTitleCardState(
        inputs = arrayOf(tag?.id),
        initialText = detail?.title.orEmpty(),
    )
    val descriptionCardState = rememberDescriptionCardState(
        inputs = arrayOf(tag?.id),
        initialText = detail?.description.orEmpty(),
        initialPage = 1,
    )
    val colorCardState = rememberColorCardState(
        inputs = arrayOf(tag?.id),
        initialColor = detail?.color?.let(::Color) ?: Color.Black,
    )

    return retain(titleCardState, descriptionCardState, colorCardState) {
        TagDetailScaffoldState(
            titleCardState = titleCardState,
            descriptionCardState = descriptionCardState,
            colorCardState = colorCardState,
        )
    }
}
