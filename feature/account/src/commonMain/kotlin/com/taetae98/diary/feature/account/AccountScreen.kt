package com.taetae98.diary.feature.account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.taetae98.diary.library.google.auth.compose.rememberGoogleAuthManager
import com.taetae98.diary.ui.compose.topbar.NavigateUpTopBar

@Composable
internal fun AccountScreen(
    modifier: Modifier,
    onNavigateUp: () -> Unit,
    uiState: State<AccountUiState>,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            NavigateUpTopBar(
                onNavigateUp = onNavigateUp,
            )
        }
    ) {
        Content(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            uiState = uiState,
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    uiState: State<AccountUiState>,
) {
    Box(
        modifier = modifier,
    ) {
        ButtonLayout(
            modifier = Modifier.align(Alignment.BottomCenter),
            uiState = uiState,
        )
    }
}

@Composable
private fun ButtonLayout(
    modifier: Modifier = Modifier,
    uiState: State<AccountUiState>,
) {
    when (uiState.value) {
        is AccountUiState.Guest -> {
            GuestButtonLayout(
                modifier = modifier,
            )
        }

        else -> Unit
    }
}

@Composable
private fun GuestButtonLayout(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        GoogleLoginButton(
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun GoogleLoginButton(
    modifier: Modifier = Modifier,
) {
    val googleAuthManager = rememberGoogleAuthManager()

    FilledIconButton(
        modifier = modifier,
        onClick = googleAuthManager::signIn,
    ) {
        Text(text = "구글 로그인")
    }
}