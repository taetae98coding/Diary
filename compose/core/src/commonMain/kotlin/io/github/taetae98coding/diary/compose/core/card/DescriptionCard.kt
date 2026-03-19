package io.github.taetae98coding.diary.compose.core.card

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownTypography
import io.github.taetae98coding.diary.compose.core.icon.MarkdownIcon
import io.github.taetae98coding.diary.compose.core.icon.TextFieldsIcon
import io.github.taetae98coding.diary.compose.core.preview.ComponentPreview
import io.github.taetae98coding.diary.compose.core.text.ClearTextField
import io.github.taetae98coding.diary.compose.core.text.transparentIndicator
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import kotlinx.coroutines.launch

@Composable
public fun DescriptionCard(
    modifier: Modifier = Modifier,
    state: DescriptionCardState = rememberDescriptionCardState(),
) {
    Card(modifier = modifier) {
        DescriptionTabLayout(
            state = state,
        )
        DescriptionPager(
            state = state,
        )
    }
}

@Composable
private fun DescriptionTabLayout(
    state: DescriptionCardState,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    PrimaryTabRow(
        selectedTabIndex = state.pagerState.currentPage,
        modifier = modifier,
    ) {
        repeat(state.pagerState.pageCount) { tabIndex ->
            Tab(
                selected = state.pagerState.currentPage == tabIndex,
                onClick = { coroutineScope.launch { state.pagerState.animateScrollToPage(tabIndex) } },
                icon = {
                    when (tabIndex) {
                        0 -> TextFieldsIcon()
                        1 -> MarkdownIcon()
                    }
                },
            )
        }
    }
}

@Composable
private fun DescriptionPager(
    state: DescriptionCardState,
    modifier: Modifier = Modifier,
) {
    SubcomposeLayout(modifier = modifier) { constraints ->
        val lineLimits = if (constraints.hasBoundedHeight || constraints.hasFixedHeight) {
            TextFieldLineLimits.MultiLine(minHeightInLines = 15)
        } else {
            TextFieldLineLimits.MultiLine(minHeightInLines = 15, maxHeightInLines = 20)
        }
        val pagerConstraints = if (constraints.hasBoundedHeight || constraints.hasFixedHeight) {
            constraints
        } else {
            val placeholderMeasurable = subcompose("placeholder") {
                DescriptionTextField(
                    state = state,
                    modifier = Modifier.fillMaxSize(),
                    lineLimits = lineLimits,
                )
            }
            val placeholderPlaceable = placeholderMeasurable.first().measure(constraints)

            constraints.copy(
                minHeight = placeholderPlaceable.height,
                maxHeight = placeholderPlaceable.height,
            )
        }

        val pagerMeasurable = subcompose("pager") {
            HorizontalPager(
                state = state.pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                when (page) {
                    0 -> {
                        DescriptionTextField(
                            state = state,
                            modifier = Modifier.fillMaxSize(),
                            lineLimits = lineLimits,
                        )
                    }

                    1 -> {
                        DescriptionMarkdown(
                            state = state,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        }
        val pagerPlaceable = pagerMeasurable.first().measure(pagerConstraints)

        layout(pagerPlaceable.width, pagerPlaceable.height) {
            pagerPlaceable.place(0, 0)
        }
    }
}

@Composable
private fun DescriptionTextField(
    state: DescriptionCardState,
    modifier: Modifier = Modifier,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
) {
    ClearTextField(
        state = state.textFieldState,
        modifier = modifier,
        label = { Text(text = "설명") },
        lineLimits = lineLimits,
        colors = TextFieldDefaults.colors().transparentIndicator(),
    )
}

@Composable
private fun DescriptionMarkdown(
    state: DescriptionCardState,
    modifier: Modifier = Modifier,
) {
    Markdown(
        content = state.textFieldState.text.toString(),
        modifier = modifier
            .verticalScroll(state = rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        typography = markdownTypography(
            h1 = DiaryTheme.typography.displayLarge.copy(fontSize = 40.sp),
            h2 = DiaryTheme.typography.displayMedium.copy(fontSize = 36.sp),
            h3 = DiaryTheme.typography.displaySmall.copy(fontSize = 32.sp),
            h4 = DiaryTheme.typography.headlineMedium.copy(fontSize = 28.sp),
            h5 = DiaryTheme.typography.headlineSmall.copy(fontSize = 24.sp),
            h6 = DiaryTheme.typography.titleLarge.copy(fontSize = 20.sp),
        ),
    )
}

@ComponentPreview
@Composable
private fun NotEmptyPreview() {
    DiaryTheme {
        DescriptionCard(state = rememberDescriptionCardState(initialText = "taetae98coding@diary.com"))
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    DiaryTheme {
        DescriptionCard()
    }
}
