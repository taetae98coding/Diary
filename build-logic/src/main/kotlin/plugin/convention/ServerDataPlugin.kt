package plugin.convention

import ext.implementation
import ext.ksp
import ext.library
import ext.libs
import ext.withDependency
import ext.withPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.project
import plugin.kotlin.KotlinJvmPlugin

internal class ServerDataPlugin : Plugin<Project> {
    private val kotlinJvmPlugin = KotlinJvmPlugin()

    override fun apply(target: Project) {
        val libs = target.libs

        kotlinJvmPlugin.apply(target)

        target.withPlugin {
            apply("com.google.devtools.ksp")
        }

        target.withDependency {
            implementation(project(":server:core:model"))

            implementation(platform(libs.library("koin-bom")))
            implementation(libs.library("koin-core"))
            implementation(platform(libs.library("koin-annotations-bom")))
            implementation(libs.library("koin-annotations"))
            ksp(platform(libs.library("koin-annotations-bom")))
            ksp(libs.library("koin-compiler"))
        }
    }
}
