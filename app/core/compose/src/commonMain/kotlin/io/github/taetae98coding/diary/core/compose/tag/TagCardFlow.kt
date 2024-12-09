package io.github.taetae98coding.diary.core.compose.tag

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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
public fun TagCardFlow(
    title: @Composable () -> Unit,
    listProvider: () -> List<TagCardItemUiState>?,
    empty: @Composable () -> Unit,
    tag: @Composable (TagCardItemUiState) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.height(IntrinsicSize.Min)) {
        val isLoading by remember { derivedStateOf { listProvider() == null } }
        val isEmpty by remember { derivedStateOf { !isLoading && listProvider().isNullOrEmpty() } }

        title()

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else if (isEmpty) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                empty()
            }
        } else {
            FlowRow(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(DiaryTheme.dimen.itemSpace),
                horizontalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(DiaryTheme.dimen.itemSpace, Alignment.CenterVertically),
            ) {
                listProvider()?.forEach {
                    key(it.id) {
                        tag(it)
                    }
                }
            }
        }
    }
}
