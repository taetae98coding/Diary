
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

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
                implementation(projects.logger.core)
                implementation(projects.logger.console.impl)
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

            buildTypes.release.proguard {
                isEnabled.set(true)
                obfuscate.set(true)
                optimize.set(false)
                joinOutputJars.set(false)
            }

            nativeDistributions {
                targetFormats(TargetFormat.Dmg)
                packageName = "Diary"
                packageVersion = BuildConfig.VERSION_NAME

                includeAllModules = true
                macOS {
                    appStore = true
                    bundleID = BuildConfig.NAMESPACE
                    infoPlist {
                        extraKeysRawXml = project.file("jvm-info.plist").readText()
                    }
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
