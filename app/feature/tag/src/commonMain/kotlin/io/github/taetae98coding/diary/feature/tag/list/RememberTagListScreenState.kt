package io.github.taetae98coding.diary.feature.tag.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope

@Composable
internal fun rememberTagListScreenState(): TagListScreenState {
    val coroutineScope = rememberCoroutineScope()

    return remember {
        TagListScreenState(coroutineScope = coroutineScope)
    }
}
