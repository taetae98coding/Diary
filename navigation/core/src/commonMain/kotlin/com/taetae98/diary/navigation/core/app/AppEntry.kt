package com.taetae98.diary.navigation.core.app

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popWhile
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.taetae98.diary.navigation.core.account.AccountEntry
import com.taetae98.diary.navigation.core.calendar.CalendarEntry
import com.taetae98.diary.navigation.core.ext.illegalRoute
import com.taetae98.diary.navigation.core.memo.MemoEntry
import com.taetae98.diary.navigation.core.more.MoreEntry
import com.taetae98.diary.navigation.core.route.AccountRoute
import com.taetae98.diary.navigation.core.route.CalendarRoute
import com.taetae98.diary.navigation.core.route.MemoRoute
import com.taetae98.diary.navigation.core.route.MoreRoute
import com.taetae98.diary.navigation.core.route.Route

public class AppEntry(
    context: ComponentContext,
) : ComponentContext by context {
    private val navigation = StackNavigation<Route>()

    public val stack: Value<ChildStack<*, ComponentContext>> = childStack(
        source = navigation,
        initialConfiguration = MemoRoute,
        handleBackButton = true,
        serializer = Route.serializer(),
        childFactory = { route, context ->
            when (route) {
                MemoRoute -> MemoEntry(
                    context = context
                )

                CalendarRoute -> CalendarEntry(
                    context = context,
                )

                MoreRoute -> MoreEntry(
                    context = context,
                    navigateToAccount = ::navigateToAccount,
                )

                AccountRoute -> AccountEntry(
                    context = context,
                    navigateUp = navigation::pop,
                )

                else -> illegalRoute(route)
            }
        }
    )

    public fun navigateToMemo() {
        navigateToAppBottomBar(MemoRoute)
    }

    public fun navigateToCalendar() {
        navigateToAppBottomBar(CalendarRoute)
    }

    public fun navigateToMore() {
        navigateToAppBottomBar(MoreRoute)
    }

    private fun navigateToAccount() {
        navigation.push(AccountRoute)
    }

    private fun navigateToAppBottomBar(route: Route) {
        navigation.popWhile { it != MemoRoute && it != route }
        navigation.bringToFront(route)
    }
}