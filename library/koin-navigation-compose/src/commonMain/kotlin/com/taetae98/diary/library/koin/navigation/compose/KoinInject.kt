package com.taetae98.diary.library.koin.navigation.compose

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.taetae98.diary.library.viewmodel.ViewModel
import kotlin.reflect.KClass
import org.koin.compose.getKoin

@Composable
public inline fun <reified T : ViewModel> ComponentContext.koinInject(): T {
    val scope = getKoin()

    return instanceKeeper.getOrCreate(scope) { scope.get() }
}

public inline fun <reified T : ViewModel> getKClassForKViewModel(): KClass<T> {
    return T::class
}