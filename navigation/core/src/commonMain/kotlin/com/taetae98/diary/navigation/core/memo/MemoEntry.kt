package com.taetae98.diary.navigation.core.memo

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.taetae98.diary.navigation.core.ext.illegalRoute
import com.taetae98.diary.navigation.core.route.MemoAddRoute
import com.taetae98.diary.navigation.core.route.MemoDetailRoute
import com.taetae98.diary.navigation.core.route.MemoListRoute
import com.taetae98.diary.navigation.core.route.Route

public class MemoEntry(
    context: ComponentContext,
) : ComponentContext by context {
    private val navigation = StackNavigation<Route>()

    public val stack: Value<ChildStack<*, ComponentContext>> = childStack(
        source = navigation,
        initialConfiguration = MemoListRoute,
        handleBackButton = true,
        serializer = Route.serializer(),
        childFactory = { route, context ->
            when (route) {
                MemoListRoute -> MemoListEntry(
                    context = context,
                    navigateToMemoAdd = ::navigateToMemoAdd,
                    navigateToMemoDetail = ::navigateToMemoDetail,
                )

                MemoAddRoute -> MemoAddEntry(
                    context = context,
                    navigateUp = ::navigateUp,
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
        navigation.push(MemoAddRoute)
    }

    private fun navigateUp() {
        navigation.pop()
    }

    private fun navigateToMemoDetail(memoId: String) {
        navigation.push(MemoDetailRoute(memoId))
    }
}