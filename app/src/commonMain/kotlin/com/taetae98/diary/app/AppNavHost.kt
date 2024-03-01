package com.taetae98.diary.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.LocalStackAnimationProvider
import com.arkivanov.decompose.extensions.compose.stack.animation.StackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.StackAnimationProvider
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.taetae98.diary.feature.account.AccountEntryPoint
import com.taetae98.diary.feature.calendar.CalendarEntryPoint
import com.taetae98.diary.feature.finished.memo.FinishedMemoEntryPoint
import com.taetae98.diary.feature.memo.MemoEntryPoint
import com.taetae98.diary.feature.more.MoreEntryPoint
import com.taetae98.diary.feature.tag.TagEntryPoint
import com.taetae98.diary.navigation.core.account.AccountEntry
import com.taetae98.diary.navigation.core.app.AppEntry
import com.taetae98.diary.navigation.core.calendar.CalendarEntry
import com.taetae98.diary.navigation.core.finished.memo.FinishedMemoEntry
import com.taetae98.diary.navigation.core.memo.MemoEntry
import com.taetae98.diary.navigation.core.more.MoreEntry
import com.taetae98.diary.navigation.core.tag.TagEntry

@Composable
internal fun AppNavHost(
    modifier: Modifier = Modifier,
    entry: AppEntry,
) {
    CompositionLocalProvider(
        LocalStackAnimationProvider provides DiaryStackAnimationProvider,
    ) {
        Children(
            modifier = modifier,
            stack = entry.stack,
        ) {
            when (val instance = it.instance) {
                is MemoEntry -> MemoEntryPoint(entry = instance)
                is MoreEntry -> MoreEntryPoint(entry = instance)
                is AccountEntry -> AccountEntryPoint(entry = instance)
                is CalendarEntry -> CalendarEntryPoint(entry = instance)
                is TagEntry -> TagEntryPoint(entry = instance)
                is FinishedMemoEntry -> FinishedMemoEntryPoint(entry = instance)
            }
        }
    }
}

private val DiaryStackAnimationProvider = object : StackAnimationProvider {
    override fun <C : Any, T : Any> provide(): StackAnimation<C, T> = stackAnimation()
}