package io.github.taetae98coding.diary.compose.core.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import io.github.taetae98coding.diary.compose.core.icon.AccountIcon
import io.github.taetae98coding.diary.compose.core.preview.ComponentPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme

@Composable
public fun ProfileImage(
    model: Any?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    shape: Shape = CircleShape,
) {
    val painter = rememberAsyncImagePainter(model)
    val state by painter.state.collectAsState()

    when (state) {
        is AsyncImagePainter.State.Success -> {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                modifier = modifier.clip(shape),
            )
        }

        else -> {
            val tint = if (isSystemInDarkTheme()) {
                Color.Black
            } else {
                Color.White
            }

            val background = if (isSystemInDarkTheme()) {
                Color.White
            } else {
                Color.Black
            }

            AccountIcon(
                modifier = modifier.background(background, shape)
                    .padding(8.dp),
                contentDescription = contentDescription,
                tint = tint,
            )
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    DiaryTheme {
        Surface {
            ProfileImage(
                model = null,
                modifier = Modifier.size(100.dp),
            )
        }
    }
}
