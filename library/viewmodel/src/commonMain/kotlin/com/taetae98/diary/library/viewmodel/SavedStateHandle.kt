package com.taetae98.diary.library.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

public class SavedStateHandle(
    private val savedState: MutableMap<String, JsonElement> = mutableMapOf()
) {
    private val stateFlowMap = mutableMapOf<String, MutableStateFlow<*>>()

    @Suppress("UNCHECKED_CAST")
    public fun <T> getStateFlow(key: String, initialValue: T): StateFlow<T> {
        val stateFlow = when (initialValue) {
            is String? -> stateFlowMap.getOrPut(key) {
                val jsonElement = savedState.getOrPut(key) { JsonPrimitive(initialValue) }
                val value = jsonElement.jsonPrimitive.contentOrNull

                MutableStateFlow(value)
            }

            is Long? -> stateFlowMap.getOrPut(key) {
                val jsonElement = savedState.getOrPut(key) { JsonPrimitive(initialValue) }
                val value = jsonElement.jsonPrimitive.contentOrNull?.toLong()

                MutableStateFlow(value)
            }

            is Boolean? -> stateFlowMap.getOrPut(key) {
                val jsonElement = savedState.getOrPut(key) { JsonPrimitive(initialValue) }
                val value = jsonElement.jsonPrimitive.contentOrNull?.toBooleanStrictOrNull()

                MutableStateFlow(value)
            }

            else -> error("Not Support Type : $initialValue")
        }

        return stateFlow.asStateFlow() as StateFlow<T>
    }

    @Suppress("UNCHECKED_CAST")
    public operator fun <T> set(key: String, value: T) {
        when (value) {
            is String? -> {
                val flow = stateFlowMap.getOrPut(key) { MutableStateFlow(value) }

                savedState[key] = JsonPrimitive(value)
                (flow as? MutableStateFlow<T>)?.value = value
            }

            is Long? -> {
                val flow = stateFlowMap.getOrPut(key) { MutableStateFlow(value) }

                savedState[key] = JsonPrimitive(value)
                (flow as? MutableStateFlow<T>)?.value = value
            }

            is Boolean? -> {
                val flow = stateFlowMap.getOrPut(key) { MutableStateFlow(value) }

                savedState[key] = JsonPrimitive(value)
                (flow as? MutableStateFlow<T>)?.value = value
            }
        }
    }
}
