package io.github.taetae98coding.diary.app.shared

import androidx.navigation3.runtime.NavKey
import io.github.taetae98coding.diary.core.navigation.CalendarHomeNavKey
import io.github.taetae98coding.diary.core.navigation.MoreHomeNavKey
import io.github.taetae98coding.diary.core.navigation.TagHomeNavKey

internal enum class TopLevelDestination(val navKey: NavKey) {
    Tag(navKey = TagHomeNavKey),
    Calendar(navKey = CalendarHomeNavKey),
    More(navKey = MoreHomeNavKey),
}
