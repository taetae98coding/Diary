import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.Family

plugins {
    id("diary.ios")
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    targets.filterIsInstance<KotlinNativeTarget>()
        .filter { it.konanTarget.family == Family.IOS }
        .forEach { target ->
            target.binaries.framework {
                baseName = "iosApp"
                isStatic = true
            }
        }

    sourceSets {
        iosMain {
            dependencies {
                implementation(project(":app"))

                implementation(compose.ui)

                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)

                implementation(libs.decompose)
            }
        }
    }
}
