package com.taetae98.diary.ui.compose.scaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
public actual fun DiaryScaffold(
    modifier: Modifier,
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    snackbarHost: @Composable () -> Unit,
    floatingActionButton: @Composable () -> Unit,
    containerColor: Color,
    contentColor: Color,
    content: @Composable (PaddingValues) -> Unit
) {
    Surface(
        modifier = modifier,
        color = containerColor,
        contentColor = contentColor,
    ) {
        Box {
            Column {
                topBar()
                Box(
                    modifier = Modifier.weight(1F)
                ) {
                    content(PaddingValues(0.dp))
                }
                bottomBar()
            }

            Box(
                modifier = Modifier.align(alignment = Alignment.BottomCenter)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    snackbarHost()
                    Box(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            top = 0.dp,
                            end = 16.dp,
                            bottom = 16.dp,
                        ).align(Alignment.End)
                    ) {
                        floatingActionButton()
                    }
                }
            }
        }
    }
}