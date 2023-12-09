package com.taetae98.diary.feature.more

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.taetae98.diary.ui.compose.modifier.itemContentPadding
import com.taetae98.diary.ui.compose.modifier.itemPadding
import com.taetae98.diary.ui.compose.modifier.scaffoldPadding
import com.taetae98.diary.ui.compose.topbar.TitleTopBar

@Composable
internal fun MoreScreen(
    modifier: Modifier = Modifier,
    onAccount: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = { TitleTopBar(title = "더보기") }
    ) {
        Content(
            modifier = Modifier.scaffoldPadding(it),
            onAccount = onAccount,
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    onAccount: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(horizontal = itemPadding)
            .verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(itemPadding)
    ) {
        TitleItem(
            title = "계정",
            onClick = onAccount,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TitleItem(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
    ) {
        Text(
            modifier = Modifier.padding(itemContentPadding),
            text = title
        )
    }
}
