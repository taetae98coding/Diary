import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
    alias(libs.plugins.diary.primitive.jvm)
    alias(libs.plugins.diary.primitive.compose)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.app.shared)
                implementation(libs.jetbrains.compose.ui)
                runtimeOnly(compose.desktop.currentOs)
            }
        }
    }
}

compose {
    desktop {
        application {
            mainClass = "io.github.taetae98coding.diary.JvmAppKt"

            nativeDistributions {
                packageName = "Diary"
                packageVersion = BuildConfig.VERSION_NAME

                macOS {
                    bundleID = BuildConfig.NAMESPACE
                }
            }
        }
    }
}

buildkonfig {
    packageName = BuildConfig.NAMESPACE

    defaultConfigs {
    }

    defaultConfigs("dev") {
        buildConfigField(type = STRING, name = "APP_NAME", value = "DiaryDev", nullable = false, const = true)
    }

    defaultConfigs("real") {
        buildConfigField(type = STRING, name = "APP_NAME", value = "Diary", nullable = false, const = true)
    }
}
