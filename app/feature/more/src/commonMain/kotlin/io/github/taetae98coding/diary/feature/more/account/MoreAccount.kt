package io.github.taetae98coding.diary.feature.more.account

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.core.design.system.chip.DiaryAssistChip
import io.github.taetae98coding.diary.core.resources.Res
import io.github.taetae98coding.diary.core.resources.guest
import io.github.taetae98coding.diary.core.resources.icon.AccountIcon
import io.github.taetae98coding.diary.core.resources.icon.LoginIcon
import io.github.taetae98coding.diary.core.resources.icon.LogoutIcon
import io.github.taetae98coding.diary.core.resources.join
import io.github.taetae98coding.diary.core.resources.login
import io.github.taetae98coding.diary.core.resources.logout
import io.github.taetae98coding.diary.feature.more.account.state.MoreAccountUiState
import io.github.taetae98coding.diary.library.color.multiplyAlpha
import io.github.taetae98coding.diary.library.shimmer.m3.shimmer
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun MoreAccount(
    uiStateProvider: () -> MoreAccountUiState,
    onLogin: () -> Unit,
    onJoin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ProfileRow(
                uiStateProvider = uiStateProvider,
                modifier = Modifier.padding(horizontal = 8.dp),
            )

            ButtonRow(
                uiStateProvider = uiStateProvider,
                onLogin = onLogin,
                onJoin = onJoin,
            )
        }
    }
}

@Composable
private fun ProfileRow(
    uiStateProvider: () -> MoreAccountUiState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProfileImage()
        Email(uiStateProvider = uiStateProvider)
    }
}

@Composable
private fun ProfileImage(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.size(54.dp)
            .background(
                color = LocalContentColor.current.multiplyAlpha(value = 0.38F),
                shape = CircleShape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        AccountIcon(modifier = Modifier.size(40.dp))
    }
}

@Composable
private fun Email(
    uiStateProvider: () -> MoreAccountUiState,
    modifier: Modifier = Modifier,
) {
    Crossfade(targetState = uiStateProvider()) { uiState ->
        when (uiState) {
            is MoreAccountUiState.Loading -> {
                Text(
                    text = "",
                    modifier = modifier.width(100.dp)
                        .shimmer(),
                )
            }

            is MoreAccountUiState.Guest -> {
                Text(
                    text = stringResource(Res.string.guest),
                    modifier = modifier,
                )
            }

            is MoreAccountUiState.Member -> {
                Text(
                    text = uiState.email,
                    modifier = modifier,
                )
            }
        }
    }
}

@Composable
private fun ButtonRow(
    uiStateProvider: () -> MoreAccountUiState,
    onLogin: () -> Unit,
    onJoin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        when (val uiState = uiStateProvider()) {
            is MoreAccountUiState.Loading -> {
                items(
                    count = 1,
                    key = { it },
                    contentType = { "placeHolder" },
                ) {
                    DiaryAssistChip(
                        onClick = {},
                        label = { Text(text = "") },
                        modifier = Modifier.width(80.dp).animateItem(),
                        shape = CircleShape,
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = LocalContentColor.current.multiplyAlpha(0.38F),
                        ),
                        border = null,
                    )
                }
            }

            is MoreAccountUiState.Guest -> {
                item(
                    key = "login",
                    contentType = "Chip",
                ) {
                    DiaryAssistChip(
                        onClick = onLogin,
                        label = { Text(text = stringResource(Res.string.login)) },
                        modifier = Modifier.animateItem(),
                        leadingIcon = { LoginIcon() },
                        shape = CircleShape,
                    )
                }

                item(
                    key = "join",
                    contentType = "Chip",
                ) {
                    DiaryAssistChip(
                        onClick = onJoin,
                        label = { Text(text = stringResource(Res.string.join)) },
                        modifier = Modifier.animateItem(),
                        leadingIcon = { AccountIcon() },
                        shape = CircleShape,
                    )
                }
            }

            is MoreAccountUiState.Member -> {
                item(
                    key = "logout",
                    contentType = "Chip",
                ) {
                    DiaryAssistChip(
                        onClick = uiState.logout,
                        label = { Text(text = stringResource(Res.string.logout)) },
                        modifier = Modifier.animateItem(),
                        leadingIcon = { LogoutIcon() },
                        shape = CircleShape,
                    )
                }
            }
        }
    }
}
