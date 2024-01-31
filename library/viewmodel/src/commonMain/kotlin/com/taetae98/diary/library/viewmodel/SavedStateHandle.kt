package com.taetae98.diary.library.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement

public class SavedStateHandle(
    public val savedState: MutableMap<String, JsonElement> = mutableMapOf()
) {
    public val stateFlowMap: MutableMap<String, MutableStateFlow<*>> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    public inline fun <reified T> getStateFlow(key: String, initialValue: T): StateFlow<T> {
        val mutableStateFlow = stateFlowMap.getOrPut(key) {
            val jsonElement = savedState.getOrPut(key) { initialValue.toJsonElement() }
            val value = jsonElement.toValue<T>()

            MutableStateFlow(value)
        }

        return mutableStateFlow.asStateFlow() as StateFlow<T>
    }

    @Suppress("UNCHECKED_CAST")
    public inline operator fun <reified T> set(key: String, value: T) {
        val flow = stateFlowMap.getOrPut(key) { MutableStateFlow(value) }

        savedState[key] = value.toJsonElement()
        (flow as? MutableStateFlow<T>)?.value = value
    }

    public inline fun <reified T> T.toJsonElement(): JsonElement {
        return Json.encodeToJsonElement(this)
    }

    public inline fun <reified T> JsonElement.toValue(): T {
        return Json.decodeFromJsonElement<T>(this)
    }
}
