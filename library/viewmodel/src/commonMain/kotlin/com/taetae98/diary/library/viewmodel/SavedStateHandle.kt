package com.taetae98.diary.library.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long

public class SavedStateHandle(
    private val savedState: MutableMap<String, JsonElement> = mutableMapOf()
) {
    private val stateFlowMap = mutableMapOf<String, MutableStateFlow<*>>()

    @Suppress("UNCHECKED_CAST")
    public fun <T : Any> getStateFlow(key: String, initialValue: T): StateFlow<T> {
        val mutableStateFlow = stateFlowMap.getOrPut(key) {
            val jsonElement = savedState.getOrPut(key) { initialValue.toJsonElement() }
            val value = jsonElement.toValue(initialValue)

            MutableStateFlow(value)
        }

        return mutableStateFlow.asStateFlow() as StateFlow<T>
    }

    @Suppress("UNCHECKED_CAST")
    public operator fun <T : Any> set(key: String, value: T) {
        val flow = stateFlowMap.getOrPut(key) { MutableStateFlow(value) }

        savedState[key] = value.toJsonElement()
        (flow as? MutableStateFlow<T>)?.value = value
    }

    private fun <T : Any> T.toJsonElement(): JsonElement {
        return when (this) {
            is Boolean -> JsonPrimitive(this)
            is Long -> JsonPrimitive(this)
            is String -> JsonPrimitive(this)
            is Set<*> -> JsonArray(requireNoNulls().map { it.toJsonElement() })
            else -> error("Not Support Type : $this")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified T : Any> JsonElement.toValue(initialValue: T): T {
        return when (initialValue) {
            is Boolean -> jsonPrimitive.boolean
            is Long -> jsonPrimitive.long
            is String -> jsonPrimitive.content
            is Set<*> -> Json.decodeFromJsonElement<T>(this)
            else -> error("Not Support Type : $initialValue")
        } as T
    }
}
