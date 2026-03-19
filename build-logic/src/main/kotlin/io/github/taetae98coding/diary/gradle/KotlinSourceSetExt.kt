package io.github.taetae98coding.diary.gradle

import org.gradle.api.NamedDomainObjectContainer
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

public fun NamedDomainObjectContainer<KotlinSourceSet>.nonAndroidMain(block: KotlinSourceSet.() -> Unit) {
    val commonMain = getByName("commonMain")
    val appleMain = findByName("appleMain")
    val jvmMain = findByName("jvmMain")
    val wasmJsMain = findByName("wasmJsMain")
    val nonAndroidMain = maybeCreate("nonAndroidMain")

    nonAndroidMain.dependsOn(commonMain)
    appleMain?.dependsOn(nonAndroidMain)
    jvmMain?.dependsOn(nonAndroidMain)
    wasmJsMain?.dependsOn(nonAndroidMain)

    nonAndroidMain.apply(block)
}

public fun NamedDomainObjectContainer<KotlinSourceSet>.nonAppleMain(block: KotlinSourceSet.() -> Unit) {
    val commonMain = getByName("commonMain")
    val androidMain = findByName("androidMain")
    val jvmMain = findByName("jvmMain")
    val wasmJsMain = findByName("wasmJsMain")
    val nonAppleMain = maybeCreate("nonAppleMain")

    nonAppleMain.dependsOn(commonMain)
    androidMain?.dependsOn(nonAppleMain)
    jvmMain?.dependsOn(nonAppleMain)
    wasmJsMain?.dependsOn(nonAppleMain)

    nonAppleMain.apply(block)
}

public fun NamedDomainObjectContainer<KotlinSourceSet>.nonWasmMain(block: KotlinSourceSet.() -> Unit) {
    val commonMain = getByName("commonMain")
    val androidMain = findByName("androidMain")
    val appleMain = findByName("appleMain")
    val jvmMain = findByName("jvmMain")
    val nonWasmMain = maybeCreate("nonWasmMain")

    nonWasmMain.dependsOn(commonMain)
    androidMain?.dependsOn(nonWasmMain)
    appleMain?.dependsOn(nonWasmMain)
    jvmMain?.dependsOn(nonWasmMain)

    nonWasmMain.apply(block)
}
