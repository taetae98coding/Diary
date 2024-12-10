package io.github.taetae98coding.diary.core.compose.topbar

import androidx.compose.foundation.basicMarquee
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
public fun TopBarTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier.basicMarquee(iterations = Int.MAX_VALUE),
        maxLines = 1,
    )
}
