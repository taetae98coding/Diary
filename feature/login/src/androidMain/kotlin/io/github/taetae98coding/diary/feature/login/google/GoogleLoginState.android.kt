package io.github.taetae98coding.diary.feature.login.google

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
internal actual fun rememberGoogleLoginState(onLogin: () -> Unit): GoogleLoginState {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            onLogin()
        }
    }
    return remember(launcher) {
        object : GoogleLoginState {
            override fun login() {
                val intent = Intent(Settings.ACTION_ADD_ACCOUNT)
                    .putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))

                launcher.launch(intent)
            }
        }
    }
}
