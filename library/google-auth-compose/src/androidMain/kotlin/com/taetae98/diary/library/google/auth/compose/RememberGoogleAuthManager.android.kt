package com.taetae98.diary.library.google.auth.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.taetae98.diary.library.google.auth.api.GoogleAuthManager
import com.taetae98.diary.library.google.auth.impl.GoogleAuthManagerImpl

@Composable
public actual fun rememberGoogleAuthManager(): GoogleAuthManager {
    val context = LocalContext.current

    return remember {
        GoogleAuthManagerImpl(context)
    }
}