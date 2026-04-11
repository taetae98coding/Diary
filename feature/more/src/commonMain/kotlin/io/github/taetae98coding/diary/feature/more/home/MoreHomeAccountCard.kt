package io.github.taetae98coding.diary.feature.more.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.compose.core.chip.DiaryAssistChip
import io.github.taetae98coding.diary.compose.core.icon.LoginIcon
import io.github.taetae98coding.diary.compose.core.icon.LogoutIcon
import io.github.taetae98coding.diary.compose.core.image.ProfileImage
import io.github.taetae98coding.diary.compose.core.preview.ComponentPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme

@Composable
internal fun MoreHomeAccountCard(
    modifier: Modifier = Modifier,
    uiStateProvider: () -> MoreHomeAccountUiState = { MoreHomeAccountUiState.Loading },
    onLogin: () -> Unit = {},
    onLogout: () -> Unit = {},
) {
    Card(modifier = modifier) {
        ProfileRow(
            uiStateProvider = uiStateProvider,
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp),
        )
        ActionRow(
            uiStateProvider = uiStateProvider,
            onLogin = onLogin,
            onLogout = onLogout,
        )
    }
}

@Composable
private fun ProfileRow(
    uiStateProvider: () -> MoreHomeAccountUiState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val uiState = uiStateProvider()
        val profileImage = when (uiState) {
            is MoreHomeAccountUiState.Loading -> null
            is MoreHomeAccountUiState.NotLogin -> "게스트"
            is MoreHomeAccountUiState.Login -> uiState.profileImage
        }
        val email = when (uiState) {
            is MoreHomeAccountUiState.Loading -> null
            is MoreHomeAccountUiState.NotLogin -> "게스트"
            is MoreHomeAccountUiState.Login -> uiState.email
        }

        ProfileImage(
            model = profileImage,
            modifier = Modifier.size(40.dp),
        )
        email?.let {
            Text(
                text = it,
                style = DiaryTheme.typography.titleMediumEmphasized,
            )
        }
    }
}

@Composable
private fun ActionRow(
    modifier: Modifier = Modifier,
    uiStateProvider: () -> MoreHomeAccountUiState = { MoreHomeAccountUiState.Loading },
    onLogin: () -> Unit = {},
    onLogout: () -> Unit = {},
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
    ) {
        when (uiStateProvider()) {
            is MoreHomeAccountUiState.Loading -> {
                item {
                    DiaryAssistChip(
                        onClick = {},
                        label = {},
                        modifier = Modifier.animateItem()
                            .width(80.dp),
                    )
                }
            }

            is MoreHomeAccountUiState.NotLogin -> {
                item {
                    DiaryAssistChip(
                        onClick = onLogin,
                        label = { Text(text = "로그인") },
                        leadingIcon = { LoginIcon() },
                        modifier = Modifier.animateItem(),
                    )
                }
            }

            is MoreHomeAccountUiState.Login -> {
                item {
                    DiaryAssistChip(
                        onClick = onLogout,
                        label = { Text(text = "로그아웃") },
                        leadingIcon = { LogoutIcon() },
                        modifier = Modifier.animateItem(),
                    )
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun LoadingPreview() {
    DiaryTheme {
        MoreHomeAccountCard(
            uiStateProvider = { MoreHomeAccountUiState.Loading },
        )
    }
}

@ComponentPreview
@Composable
private fun NotLoginPreview() {
    DiaryTheme {
        MoreHomeAccountCard(
            uiStateProvider = { MoreHomeAccountUiState.NotLogin },
        )
    }
}

@ComponentPreview
@Composable
private fun LoginPreview() {
    DiaryTheme {
        MoreHomeAccountCard(
            uiStateProvider = {
                MoreHomeAccountUiState.Login(
                    email = "taetae98coding@diary.com",
                    profileImage = null,
                )
            },
        )
    }
}
