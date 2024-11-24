package io.github.taetae98coding.diary.core.compose.dialog

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalComposeUiApi::class)
public actual fun transparentScrimDialogProperties(): DialogProperties = DialogProperties(scrimColor = Color.Transparent)
