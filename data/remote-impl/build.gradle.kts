import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.compose.internal.utils.getLocalProperty

plugins {
    id("diary.multiplatform")
    id("diary.koin.multiplatform")
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    sourceSets {
        val nonAndroidMain = maybeCreate("nonAndroidMain")

        nonAndroidMain.dependsOn(commonMain.get())
        iosMain.get().dependsOn(nonAndroidMain)
        jvmMain.get().dependsOn(nonAndroidMain)
        jsMain.get().dependsOn(nonAndroidMain)

        commonMain {
            dependencies {
                implementation(project(":data:dto"))
                implementation(project(":data:remote-api"))

                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization)

                implementation(libs.bundles.ktor)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.data.remote.impl"

    buildTypes {
        debug {
            buildConfigField(
                type = "String",
                name = "OPEN_API_HOLIDAY_URL",
                value = "\"${getLocalProperty("open.api.debug.holiday.url")}\""
            )

            buildConfigField(
                type = "String",
                name = "OPEN_API_HOLIDAY_KEY",
                value = "\"${getLocalProperty("open.api.debug.holiday.key")}\""
            )
        }

        release {
            buildConfigField(
                type = "String",
                name = "OPEN_API_HOLIDAY_URL",
                value = "\"${getLocalProperty("open.api.release.holiday.url")}\""
            )

            buildConfigField(
                type = "String",
                name = "OPEN_API_HOLIDAY_KEY",
                value = "\"${getLocalProperty("open.api.release.holiday.key")}\""
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

buildkonfig {
    packageName = "${Build.NAMESPACE}.data.remote.impl"

    defaultConfigs {

    }

    targetConfigs("debug") {
        create("nonAndroid") {
            buildConfigField(
                type = FieldSpec.Type.STRING,
                name = "OPEN_API_HOLIDAY_URL",
                value = getLocalProperty("open.api.debug.holiday.url"),
                const = true
            )

            buildConfigField(
                type = FieldSpec.Type.STRING,
                name = "OPEN_API_HOLIDAY_KEY",
                value = getLocalProperty("open.api.debug.holiday.key"),
                const = true
            )
        }
    }

    targetConfigs("release") {
        create("nonAndroid") {
            buildConfigField(
                type = FieldSpec.Type.STRING,
                name = "OPEN_API_HOLIDAY_URL",
                value = getLocalProperty("open.api.release.holiday.url"),
                const = true
            )

            buildConfigField(
                type = FieldSpec.Type.STRING,
                name = "OPEN_API_HOLIDAY_KEY",
                value = getLocalProperty("open.api.release.holiday.key"),
                const = true
            )
        }
    }
}
