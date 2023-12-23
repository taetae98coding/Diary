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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.taetae98.diary.domain.entity.account.account.Credential
import com.taetae98.diary.library.google.sign.api.GoogleCredential
import com.taetae98.diary.library.google.sign.compose.rememberGoogleAuthManager
import com.taetae98.diary.ui.compose.topbar.NavigateUpTopBar
import kotlinx.coroutines.launch

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
    Column(
        modifier = modifier,
    ) {
        UserInfoLayout(
            modifier = Modifier.fillMaxWidth()
                .weight(1F),
            uiState = uiState
        )
        ButtonLayout(uiState = uiState)
    }
}

@Composable
private fun UserInfoLayout(
    modifier: Modifier = Modifier,
    uiState: State<AccountUiState>,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        when (val value = uiState.value) {
            is AccountUiState.Member -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = value.uid)
                    value.email?.let { Text(text = it) }
                }
            }

            else -> Unit
        }
    }
}

@Composable
private fun ButtonLayout(
    modifier: Modifier = Modifier,
    uiState: State<AccountUiState>,
) {
    when (val value = uiState.value) {
        is AccountUiState.Guest -> {
            GuestButtonLayout(
                modifier = modifier,
                onSignIn = value.onSignIn,
            )
        }

        is AccountUiState.Member -> {
            MemberButtonLayout(
                modifier = modifier,
                onSignOut = value.onSignOut,
            )
        }
    }
}

@Composable
private fun GuestButtonLayout(
    modifier: Modifier = Modifier,
    onSignIn: (Credential) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        GoogleLoginButton(
            modifier = Modifier.fillMaxWidth(),
            onSignIn = onSignIn,
        )
    }
}

@Composable
private fun GoogleLoginButton(
    modifier: Modifier = Modifier,
    onSignIn: (Credential) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val googleAuthManager = rememberGoogleAuthManager()
    val onClick = remember<() -> Unit> {
        {
            coroutineScope.launch {
                googleAuthManager.signIn()?.toDomain()?.let(onSignIn)
            }
        }
    }

    FilledIconButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Text(text = "구글 로그인")
    }
}

@Composable
private fun MemberButtonLayout(
    modifier: Modifier = Modifier,
    onSignOut: () -> Unit,
) {
    FilledIconButton(
        modifier = modifier.fillMaxWidth(),
        onClick = onSignOut
    ) {
        Text(text = "로그아웃")
    }
}

private fun GoogleCredential.toDomain(): Credential {
    return Credential.Google(
        idToken = idToken,
        accessToken = accessToken,
    )
}