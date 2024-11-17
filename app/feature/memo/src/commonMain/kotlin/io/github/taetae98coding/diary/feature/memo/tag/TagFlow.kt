package io.github.taetae98coding.diary.feature.memo.tag

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun TagFlow(
    title: @Composable () -> Unit,
    listProvider: () -> List<TagUiState>?,
    empty: @Composable () -> Unit,
    tag: @Composable (TagUiState) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.height(IntrinsicSize.Min)) {
        val list = listProvider()

        title()

        if (list == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else if (list.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                empty()
            }
        } else {
            FlowRow(
                modifier = Modifier.fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(DiaryTheme.dimen.itemSpace),
                horizontalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace, Alignment.CenterVertically),
            ) {
                list.forEach {
                    key(it.id) {
                        tag(it)
                    }
                }
            }
        }
    }
}