plugins {
    alias(libs.plugins.kotlin.multiplatform).apply(false)
    alias(libs.plugins.kotlin.android).apply(false)
    alias(libs.plugins.kotlin.jvm).apply(false)

    alias(libs.plugins.kotlin.cocoapods).apply(false)
    alias(libs.plugins.kotlin.serialization).apply(false)
    alias(libs.plugins.ksp).apply(false)

    alias(libs.plugins.compose).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)
    alias(libs.plugins.room).apply(false)
    alias(libs.plugins.kotest).apply(false)

    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.android.library).apply(false)
    alias(libs.plugins.android.firebase.crashlytics).apply(false)
    alias(libs.plugins.android.firebase.perf).apply(false)
    alias(libs.plugins.google.services).apply(false)

    alias(libs.plugins.ktor.server).apply(false)

    alias(libs.plugins.dependency.guard).apply(false)
    alias(libs.plugins.buildkonfig).apply(false)
    alias(libs.plugins.spotless)
    alias(libs.plugins.module.graph)
}

subprojects {
    afterEvaluate {
        if (isKotlinProject()) {
            plugins.apply("com.diffplug.spotless")

            spotless {
                kotlin {
                    target("**/*.kt")
                    targetExclude("**/build/**")

                    ktlint()
                    endWithNewline()
                    indentWithTabs()
                    trimTrailingWhitespace()
                }
            }
        }
    }
}

subprojects {
    afterEvaluate {
        plugins.apply("com.jraska.module.graph.assertion")

        moduleGraphAssert {
            configurations += setOf("commonMainImplementation", "commonMainApi", "implementation", "api")
        }
    }
}

subprojects {
    afterEvaluate {
        if (pluginManager.findPlugin("org.jetbrains.kotlin.native.cocoapods") != null) {
            tasks.register("buildCocoapods") {
                dependsOn(tasks.getByName("build"))
            }
        }
    }
}

fun Project.isMultiplatformProject(): Boolean {
    return plugins.findPlugin("org.jetbrains.kotlin.multiplatform") != null
}

fun Project.isJvmProject(): Boolean {
    return plugins.findPlugin("org.jetbrains.kotlin.jvm") != null
}

fun Project.isAndroidProject(): Boolean {
    return plugins.findPlugin("org.jetbrains.kotlin.android") != null
}

fun Project.isKotlinProject(): Boolean {
    return isMultiplatformProject() || isJvmProject() || isAndroidProject()
}
