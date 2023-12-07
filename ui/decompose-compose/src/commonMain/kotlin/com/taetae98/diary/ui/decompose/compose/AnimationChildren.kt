package com.taetae98.diary.ui.decompose.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

@Composable
public fun <C : Any, T : Any> AnimationChildren(
    modifier: Modifier = Modifier,
    stack: Value<ChildStack<C, T>>,
    content: @Composable (child: Child.Created<C, T>) -> Unit,
) {
    Children(
        modifier = modifier,
        stack = stack,
        content = content,
        animation = stackAnimation(),
    )
}