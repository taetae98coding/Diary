package io.github.taetae98coding.diary.gradle

import org.gradle.kotlin.dsl.DependencyHandlerScope

internal fun DependencyHandlerScope.androidRuntimeClasspath(dependencyNotation: Any) {
    add("androidRuntimeClasspath", dependencyNotation)
}

public fun DependencyHandlerScope.kspAll(dependencyNotation: Any) {
    kspAndroid(dependencyNotation)
    kspIos(dependencyNotation)
    kspJvm(dependencyNotation)
    kspWasmJs(dependencyNotation)
}

internal fun DependencyHandlerScope.kspAndroid(dependencyNotation: Any) {
    add("kspAndroid", dependencyNotation)
}

internal fun DependencyHandlerScope.kspIos(dependencyNotation: Any) {
    add("kspIosArm64", dependencyNotation)
    add("kspIosSimulatorArm64", dependencyNotation)
}

internal fun DependencyHandlerScope.kspJvm(dependencyNotation: Any) {
    add("kspJvm", dependencyNotation)
}

internal fun DependencyHandlerScope.kspWasmJs(dependencyNotation: Any) {
    add("kspWasmJs", dependencyNotation)
}
