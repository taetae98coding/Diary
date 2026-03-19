package io.github.taetae98coding.diary.gradle

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun KotlinMultiplatformExtension.android(block: KotlinMultiplatformAndroidLibraryExtension.() -> Unit) {
    extensions.configure<KotlinMultiplatformAndroidLibraryExtension>(block)
}
