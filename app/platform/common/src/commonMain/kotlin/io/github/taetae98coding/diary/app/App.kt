package io.github.taetae98coding.diary.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import diary.app.platform.common.generated.resources.Res
import diary.app.platform.common.generated.resources.message
import org.jetbrains.compose.resources.stringResource

@Composable
public fun App() {
    Box(
        modifier = Modifier.fillMaxSize()
            .imePadding(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = stringResource(Res.string.message))
    }
}
