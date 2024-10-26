import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.dependency.guard)
}

kotlin {
    explicitApi()
    jvmToolchain(17)
}

compose {
    desktop {
        application {
            mainClass = "io.github.taetae98coding.diary.JvmAppKt"

            nativeDistributions {
                targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)

                packageName = "Diary"
                packageVersion = "1.0.0"
            }

            buildTypes {
                release {
                    proguard {
                        isEnabled.set(false)
                    }
                }
            }
        }
    }
}

dependencies {
    implementation(project(":app:platform:common"))

    implementation(compose.desktop.currentOs)
}

dependencyGuard {
    configuration("runtimeClasspath") {
        allowedFilter = { dependencyName ->
            !dependencyName.contains("junit") && !dependencyName.contains("leakcanary")
        }

        baselineMap = { dependencyName ->
            dependencyName.takeUnless { it.contains("macos") || it.contains("linux") }
        }
    }
}
