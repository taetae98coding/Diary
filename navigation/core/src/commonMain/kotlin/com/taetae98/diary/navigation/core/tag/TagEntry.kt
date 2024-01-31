package com.taetae98.diary.navigation.core.tag

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.taetae98.diary.navigation.core.ext.illegalRoute
import com.taetae98.diary.navigation.core.route.Route
import com.taetae98.diary.navigation.core.route.TagAddRoute
import com.taetae98.diary.navigation.core.route.TagListRoute
import com.taetae98.diary.navigation.core.route.TagMemoRoute

public class TagEntry(
    context: ComponentContext,
) : ComponentContext by context {
    private val navigation = StackNavigation<Route>()

    public val stack: Value<ChildStack<*, ComponentContext>> = childStack(
        source = navigation,
        initialConfiguration = TagListRoute,
        handleBackButton = true,
        serializer = Route.serializer(),
        childFactory = { route, context ->
            when (route) {
                TagListRoute -> TagListEntry(
                    context = context,
                    navigateToTagAdd = ::navigateToTagAdd,
                    navigateToTagMemo = ::navigateToTagMemo,
                )

                TagAddRoute -> TagAddEntry(
                    context = context,
                    navigateUp = ::navigateUp,
                )

                is TagMemoRoute -> TagMemoEntry(
                    context = context,
                    navigateUp = ::navigateUp,
                    tagId = route.tagId,
                )

                else -> illegalRoute(route)
            }
        }
    )

    private fun navigateToTagAdd() {
        navigation.push(TagAddRoute)
    }

    private fun navigateToTagMemo(tagId: String) {
        navigation.push(TagMemoRoute(tagId))
    }

    private fun navigateUp() {
        navigation.pop()
    }
}
