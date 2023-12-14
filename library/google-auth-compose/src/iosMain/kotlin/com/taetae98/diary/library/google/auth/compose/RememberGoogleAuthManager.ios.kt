package com.taetae98.diary.library.google.auth.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.interop.LocalUIViewController
import com.taetae98.diary.library.google.auth.api.GoogleAuthManager
import com.taetae98.diary.library.google.auth.impl.GoogleAuthManagerImpl
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
@Composable
public actual fun rememberGoogleAuthManager(): GoogleAuthManager {
    val uiViewController = LocalUIViewController.current

    return remember {
        GoogleAuthManagerImpl(uiViewController)
    }
}