package com.taetae98.diary.library.google.sign.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.interop.LocalUIViewController
import com.taetae98.diary.library.google.sign.api.GoogleAuthManager
import com.taetae98.diary.library.google.sign.impl.GoogleAuthManagerImpl
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
@Composable
public actual fun rememberGoogleAuthManager(): GoogleAuthManager {
    val uiViewController = LocalUIViewController.current

    return remember {
        GoogleAuthManagerImpl(uiViewController)
    }
}