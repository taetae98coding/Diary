package com.taetae98.diary.library.google.sign.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.taetae98.diary.library.google.sign.api.GoogleAuthManager
import com.taetae98.diary.library.google.sign.impl.GoogleAuthManagerImpl

@Composable
public actual fun rememberGoogleAuthManager(): GoogleAuthManager {
    return remember { GoogleAuthManagerImpl() }
}