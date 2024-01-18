package com.taetae98.diary.feature.tag.list

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.taetae98.diary.ui.compose.button.AddFloatingButton
import com.taetae98.diary.ui.compose.scaffold.DiaryScaffold
import com.taetae98.diary.ui.compose.topbar.TitleTopBar

@Composable
internal fun TagListScreen(
    modifier: Modifier = Modifier,
    onAdd: () -> Unit,
) {
    DiaryScaffold(
        modifier = modifier,
        topBar = { TopBar() },
        floatingActionButton = { AddFloatingButton(onClick = onAdd) }
    ) {
        Content(modifier = Modifier.padding(it))
    }
}

@Composable
private fun TopBar(
    modifier: Modifier = Modifier
) {
    TitleTopBar(
        modifier = modifier,
        title = "태그"
    )
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
) {

}