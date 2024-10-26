plugins {
    alias(libs.plugins.kotlin.multiplatform).apply(false)
    alias(libs.plugins.kotlin.android).apply(false)
    alias(libs.plugins.kotlin.jvm).apply(false)

    alias(libs.plugins.compose).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)

    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.android.library).apply(false)

    alias(libs.plugins.ktor.server).apply(false)

    alias(libs.plugins.spotless)
    alias(libs.plugins.module.graph)
}

subprojects {
    afterEvaluate {
        if (isKotlinProject()) {
            plugins.apply("com.diffplug.spotless")

            spotless {
                kotlin {
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
        if (isMultiplatformProject()) {
            plugins.apply("com.jraska.module.graph.assertion")

            moduleGraphAssert {
                configurations += setOf("commonMainImplementation", "commonMainApi")
            }
        } else if (isAndroidProject() || isJvmProject()) {
            plugins.apply("com.jraska.module.graph.assertion")

            moduleGraphAssert {
                configurations += setOf("implementation", "api")
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
