package com.taetae98.diary.feature.tag

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.taetae98.diary.navigation.core.tag.TagEntry

@Composable
public fun TagEntryPoint(
    modifier: Modifier = Modifier,
    entry: TagEntry,
) {
    TagListRoute(modifier = modifier)
}