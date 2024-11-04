package plugin.convention

import ext.implementation
import ext.library
import ext.libs
import ext.withDependency
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.project
import plugin.kotlin.KotlinJvmPlugin

internal class ServerFeaturePlugin : Plugin<Project> {
    private val kotlinJvmPlugin = KotlinJvmPlugin()

    override fun apply(target: Project) {
        val libs = target.libs

        kotlinJvmPlugin.apply(target)

        target.withDependency {
            implementation(project(":server:core:model"))
            implementation(project(":common:model"))
            implementation(project(":common:exception"))

            implementation(libs.library("ktor-server-core"))
            implementation(libs.library("ktor-server-auth-jwt"))

            implementation(platform(libs.library("koin-bom")))
            implementation(libs.library("koin-ktor"))
        }
    }
}
