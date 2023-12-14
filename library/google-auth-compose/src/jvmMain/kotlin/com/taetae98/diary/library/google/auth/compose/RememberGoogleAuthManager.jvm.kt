package com.taetae98.diary.library.google.auth.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.taetae98.diary.library.google.auth.api.GoogleAuthManager
import com.taetae98.diary.library.google.auth.impl.GoogleAuthManagerImpl

@Composable
public actual fun rememberGoogleAuthManager(): GoogleAuthManager {
    return remember { GoogleAuthManagerImpl() }
}