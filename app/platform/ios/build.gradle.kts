
import com.codingfeline.buildkonfig.compiler.FieldSpec
import ext.getLocalProperty

private val localProperties = requireNotNull(project.getLocalProperty())

plugins {
    id("diary.kotlin.multiplatform")
    id("diary.compose")
    alias(libs.plugins.buildkonfig)
}

kotlin {
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries {
            framework {
                baseName = "Kotlin"
                isStatic = true
            }
        }
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":app:platform:common"))
                implementation(project(":app:core:coroutines"))
                implementation(project(":app:core:diary-service"))
                implementation(project(":app:core:holiday-service"))

                implementation(compose.ui)
                implementation(libs.lifecycle.compose)

                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)

                implementation(libs.ktor.client.darwin)
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
