package com.taetae98.diary.navigation.core.memo

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.taetae98.diary.navigation.core.ext.illegalRoute
import com.taetae98.diary.navigation.core.route.MemoAdd
import com.taetae98.diary.navigation.core.route.MemoList
import com.taetae98.diary.navigation.core.route.Route

public class MemoEntry(
    context: ComponentContext,
) : ComponentContext by context {
    private val navigation = StackNavigation<Route>()

    public val stack: Value<ChildStack<*, ComponentContext>> = childStack(
        source = navigation,
        initialConfiguration = MemoList,
        handleBackButton = true,
        serializer = Route.serializer(),
        childFactory = { route, context ->
            when (route) {
                MemoList -> MemoListEntry(
                    context = context,
                    navigateToMemoAdd = ::navigateToMemoAdd,
                )

                MemoAdd -> MemoAddEntry(
                    context = context,
                    navigateUp = navigation::pop,
                )

                else -> illegalRoute(route)
            }
        }
    )

    private fun navigateToMemoAdd() {
        navigation.push(MemoAdd)
    }
}