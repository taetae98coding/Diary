package com.taetae98.diary.navigation.core.memo

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.backStack
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.taetae98.diary.navigation.core.ext.illegalRoute
import com.taetae98.diary.navigation.core.route.MemoAddRoute
import com.taetae98.diary.navigation.core.route.MemoDetailRoute
import com.taetae98.diary.navigation.core.route.MemoListRoute
import com.taetae98.diary.navigation.core.route.Route

public class MemoEntry internal constructor(
    context: ComponentContext,
    initialRoute: Route?,
    private val finish: () -> Unit,
) : ComponentContext by context {
    private val navigation = StackNavigation<Route>()

    public val stack: Value<ChildStack<*, ComponentContext>> = childStack(
        source = navigation,
        initialConfiguration = initialRoute ?: MemoListRoute,
        handleBackButton = true,
        serializer = Route.serializer(),
        childFactory = { route, context ->
            when (route) {
                MemoListRoute -> MemoListEntry(
                    context = context,
                    navigateToMemoAdd = ::navigateToMemoAdd,
                    navigateToMemoDetail = ::navigateToMemoDetail,
                )

                is MemoAddRoute -> MemoAddEntry(
                    context = context,
                    navigateUp = ::navigateUp,
                    dateRange = route.dateRange,
                )

                is MemoDetailRoute -> MemoDetailEntry(
                    context = context,
                    navigateUp = ::navigateUp,
                    memoId = route.memoId,
                )

                else -> illegalRoute(route)
            }
        }
    )

    private fun navigateToMemoAdd() {
        navigation.push(MemoAddRoute())
    }

    private fun navigateUp() {
        if (stack.backStack.isEmpty()) {
            finish()
        } else {
            navigation.pop()
        }
    }

    private fun navigateToMemoDetail(memoId: String) {
        navigation.push(MemoDetailRoute(memoId))
    }
}