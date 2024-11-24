package io.github.taetae98coding.diary.feature.tag.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import io.github.taetae98coding.diary.core.design.system.diary.color.rememberDiaryColorState
import io.github.taetae98coding.diary.core.design.system.diary.component.rememberDiaryComponentState
import io.github.taetae98coding.diary.core.model.tag.TagDetail

@Composable
internal fun rememberTagDetailScreenDetailState(
    onUpdate: () -> Unit,
    onDelete: () -> Unit,
    detailProvider: () -> TagDetail?,
): TagDetailScreenState.Detail {
    val detail = detailProvider()
    val coroutineScope = rememberCoroutineScope()
    val componentState = rememberDiaryComponentState(
        inputs = arrayOf(detail?.title, detail?.description),
        initialTitle = detail?.title.orEmpty(),
        initialDescription = detail?.description.orEmpty(),
    )
    val colorState = rememberDiaryColorState(
        inputs = arrayOf(detail?.color),
        initialColor = detail?.color?.let { Color(it) } ?: Color.Unspecified,
    )

    return remember(componentState, colorState) {
        TagDetailScreenState.Detail(
            onUpdate = onUpdate,
            onDelete = onDelete,
            coroutineScope = coroutineScope,
            componentState = componentState,
            colorState = colorState,
        )
    }
}
