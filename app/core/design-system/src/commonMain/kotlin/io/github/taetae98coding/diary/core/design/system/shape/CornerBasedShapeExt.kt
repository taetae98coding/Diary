package io.github.taetae98coding.diary.core.design.system.shape

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.ui.unit.dp

public fun CornerBasedShape.start(): CornerBasedShape = copy(topEnd = CornerSize(0.dp), bottomEnd = CornerSize(0.dp))

public fun CornerBasedShape.end(): CornerBasedShape = copy(topStart = CornerSize(0.dp), bottomStart = CornerSize(0.dp))
