import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.Family

plugins {
    alias(libs.plugins.diary.primitive.ios)
    alias(libs.plugins.diary.primitive.compose)
}

kotlin {
    targets.withType<KotlinNativeTarget>()
        .filter { it.konanTarget.family == Family.IOS }
        .forEach { target ->
            target.binaries {
                framework {
                    baseName = "KMP"
                    isStatic = true
                }
            }
        }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.app.shared)
                implementation(projects.logger.core)
                implementation(projects.logger.analytics.impl)
                implementation(projects.logger.console.impl)
                implementation(projects.logger.crashlytics.impl)
                implementation(libs.jetbrains.compose.ui)
            }
        }
    }
}
