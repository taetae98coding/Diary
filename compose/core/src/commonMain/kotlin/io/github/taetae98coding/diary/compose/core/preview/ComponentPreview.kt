package io.github.taetae98coding.diary.compose.core.preview

import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark

@PreviewLightDark
@Preview(uiMode = AndroidUiModes.UI_MODE_NIGHT_YES)
@Preview(uiMode = AndroidUiModes.UI_MODE_NIGHT_NO)
public annotation class ComponentPreview
