package com.taetae98.diary.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.taetae98.diary.feature.account.AccountEntryPoint
import com.taetae98.diary.feature.calendar.CalendarEntryPoint
import com.taetae98.diary.feature.memo.MemoEntryPoint
import com.taetae98.diary.feature.more.MoreEntryPoint
import com.taetae98.diary.navigation.core.account.AccountEntry
import com.taetae98.diary.navigation.core.app.AppEntry
import com.taetae98.diary.navigation.core.calendar.CalendarEntry
import com.taetae98.diary.navigation.core.memo.MemoEntry
import com.taetae98.diary.navigation.core.more.MoreEntry

@Composable
internal fun AppNavHost(
    modifier: Modifier = Modifier,
    entry: AppEntry,
) {
//    CompositionLocalProvider(
//        LocalStackAnimationProvider provides DiaryStackAnimationProvider
//    ) {
    Children(
        modifier = modifier,
        stack = entry.stack,
    ) {
        when (val instance = it.instance) {
            is MemoEntry -> MemoEntryPoint(entry = instance)
            is MoreEntry -> MoreEntryPoint(entry = instance)
            is AccountEntry -> AccountEntryPoint(entry = instance)
            is CalendarEntry -> CalendarEntryPoint(entry = instance)
        }
    }
//    }
}

//private val DiaryStackAnimationProvider = object : StackAnimationProvider {
//    override fun <C : Any, T : Any> provide(): StackAnimation<C, T> = stackAnimation()
//}