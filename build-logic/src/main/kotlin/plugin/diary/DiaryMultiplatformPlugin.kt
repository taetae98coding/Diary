package plugin.diary

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import plugin.android.AndroidLibPlugin

internal class DiaryMultiplatformPlugin : Plugin<Project> {
    private val diaryModulePlugin = DiaryModulePlugin()
    private val androidLibPlugin = AndroidLibPlugin()

    override fun apply(target: Project) {
        diaryModulePlugin.apply(target)
        androidLibPlugin.apply(target)
        applyKotlin(target.extensions.getByType())
    }

    private fun applyKotlin(extension: KotlinMultiplatformExtension) = with(extension) {
        androidTarget()
        applyDefaultHierarchyTemplate()
    }
}