package com.taetae98.diary.library.google.sign.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.taetae98.diary.library.google.sign.impl.GoogleAuthManagerImpl
import com.taetae98.diary.library.google.sign.api.GoogleAuthManager

@Composable
public actual fun rememberGoogleAuthManager(): GoogleAuthManager {
    val context = LocalContext.current

    return remember {
        GoogleAuthManagerImpl(context)
    }
}