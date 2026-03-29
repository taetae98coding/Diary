package io.github.taetae98coding.diary.app.shared

import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import io.github.taetae98coding.diary.app.shared.koin.DiaryKoinApplication
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import org.koin.compose.KoinApplication
import org.koin.dsl.KoinAppDeclaration
import org.koin.plugin.module.dsl.koinConfiguration

@Composable
public fun App(configuration: KoinAppDeclaration = {}) {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .build()
    }

    KoinApplication(
        configuration = koinConfiguration<DiaryKoinApplication> {
            configuration()
        },
    ) {
        DiaryTheme {
            AppScaffold()
        }
    }
}
