package com.taetae98.diary.library.google.auth.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.taetae98.diary.library.google.auth.api.GoogleAuthManager

@Composable
public actual fun rememberGoogleAuthManager(): GoogleAuthManager {
    return remember {
        object : GoogleAuthManager {
            override fun signIn() {

            }
        }
    }
}