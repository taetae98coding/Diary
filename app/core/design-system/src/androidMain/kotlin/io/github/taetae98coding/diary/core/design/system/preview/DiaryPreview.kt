package io.github.taetae98coding.diary.core.design.system.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "LightMode", showSystemUi = false, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL)
@Preview(name = "NightMode", showSystemUi = false, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Preview(name = "Landscape", device = "spec:parent=pixel_5,orientation=landscape")
@Preview(name = "NavigationButton", device = "spec:parent=pixel_5,navigation=buttons")
@Preview(name = "FontScale", fontScale = 2.0F)
@Preview(name = "RTL", locale = "ar")
public annotation class DiaryPreview
