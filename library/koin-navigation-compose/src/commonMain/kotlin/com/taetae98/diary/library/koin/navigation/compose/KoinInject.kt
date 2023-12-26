package com.taetae98.diary.library.koin.navigation.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.instancekeeper.getOrCreateSimple
import com.taetae98.diary.library.viewmodel.SavedStateHandle
import com.taetae98.diary.library.viewmodel.ViewModel
import kotlin.reflect.KClass
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonElement
import org.koin.compose.getKoin
import org.koin.core.parameter.ParametersHolder

@Composable
public inline fun <reified T : ViewModel> ComponentContext.koinInject(): T {
    return koinInject(persistentMapOf())
}

@Composable
public inline fun <reified T : ViewModel> ComponentContext.koinInject(
    savedState: ImmutableMap<String, JsonElement>
): T {
    val scope = getKoin()
    val key = remember { requireNotNull(getKClassForKViewModel<T>().simpleName) }
    val map = instanceKeeper.getOrCreateSimple {
        stateKeeper.consume(
            key = key,
            strategy = MapSerializer(String.serializer(), JsonElement.serializer()),
        )?.toMutableMap() ?: savedState.toMutableMap()
    }

    LaunchedEffect(this) {
        if (!stateKeeper.isRegistered(key)) {
            stateKeeper.register(
                key = key,
                strategy = MapSerializer(String.serializer(), JsonElement.serializer()),
                supplier = { map }
            )
        }
    }

    return instanceKeeper.getOrCreate(scope) {
        scope.get(
            parameters = {
                ParametersHolder(mutableListOf(SavedStateHandle(map)))
            }
        )
    }
}

public inline fun <reified T : ViewModel> getKClassForKViewModel(): KClass<T> {
    return T::class
}