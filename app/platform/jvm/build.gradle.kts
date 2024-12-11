import com.codingfeline.buildkonfig.compiler.FieldSpec
import ext.getLocalProperty
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

private val localProperties = requireNotNull(project.getLocalProperty())

plugins {
    id("diary.kotlin.multiplatform")
    id("diary.compose")
    alias(libs.plugins.buildkonfig)
}

kotlin {
    jvm()

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":app:platform:common"))
                implementation(project(":app:core:coroutines"))
                implementation(project(":app:core:diary-service"))
                implementation(project(":app:core:holiday-service"))

                implementation(project(":library:koin-room"))
                implementation(project(":library:koin-datastore"))

                implementation(compose.desktop.currentOs)
                implementation(libs.lifecycle.compose)

                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)

                runtimeOnly(libs.kotlinx.coroutines.swing)
                runtimeOnly(libs.ktor.client.okhttp)
            }
        }
    }
}

compose {
    desktop {
        application {
            mainClass = "io.github.taetae98coding.diary.JvmAppKt"

            nativeDistributions {
                includeAllModules = true
                targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)

                packageName = "Diary"
                packageVersion = "1.3.3"

                macOS {
                    appStore = true

                    bundleID = "io.github.taetae98coding.diary.jvm"
                    iconFile.set(rootProject.file("asset/icon/app_icon_mac.icns"))
                }
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

buildkonfig {
    packageName = Build.NAMESPACE

    defaultConfigs {}

    defaultConfigs("dev") {
        buildConfigField(FieldSpec.Type.STRING, "FLAVOR", "dev")
        buildConfigField(FieldSpec.Type.STRING, "DIARY_API_URL", localProperties.getProperty("diary.dev.api.base.url"))
        buildConfigField(FieldSpec.Type.STRING, "HOLIDAY_API_URL", localProperties.getProperty("holiday.dev.api.url"))
        buildConfigField(FieldSpec.Type.STRING, "HOLIDAY_API_KEY", localProperties.getProperty("holiday.dev.api.key"))
    }

    defaultConfigs("real") {
        buildConfigField(FieldSpec.Type.STRING, "FLAVOR", "real")
        buildConfigField(FieldSpec.Type.STRING, "DIARY_API_URL", localProperties.getProperty("diary.real.api.base.url"))
        buildConfigField(FieldSpec.Type.STRING, "HOLIDAY_API_URL", localProperties.getProperty("holiday.real.api.url"))
        buildConfigField(FieldSpec.Type.STRING, "HOLIDAY_API_KEY", localProperties.getProperty("holiday.real.api.key"))
    }
}

