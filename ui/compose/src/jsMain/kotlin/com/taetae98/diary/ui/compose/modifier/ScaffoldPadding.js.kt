package com.taetae98.diary.ui.compose.modifier

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier

public actual fun Modifier.scaffoldPadding(paddingValues: PaddingValues): Modifier {
    return then(padding(paddingValues))
}