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

        androidMain {
            dependencies {
                implementation(libs.ktor.okhttp)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.ktor.darwin)
            }
        }

        jvmMain {
            dependencies {
                implementation(libs.ktor.okhttp)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.data.remote.impl"

    productFlavors {
        maybeCreate("dev")
        getByName("dev") {
            buildConfigField(
                type = "String",
                name = "OPEN_API_HOLIDAY_URL",
                value = "\"${getLocalProperty("open.api.dev.holiday.url")}\""
            )

            buildConfigField(
                type = "String",
                name = "OPEN_API_HOLIDAY_KEY",
                value = "\"${getLocalProperty("open.api.dev.holiday.key")}\""
            )
        }

        maybeCreate("real")
        getByName("real") {
            buildConfigField(
                type = "String",
                name = "OPEN_API_HOLIDAY_URL",
                value = "\"${getLocalProperty("open.api.real.holiday.url")}\""
            )

            buildConfigField(
                type = "String",
                name = "OPEN_API_HOLIDAY_KEY",
                value = "\"${getLocalProperty("open.api.real.holiday.key")}\""
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

    targetConfigs("dev") {
        create("nonAndroid") {
            buildConfigField(
                type = FieldSpec.Type.STRING,
                name = "OPEN_API_HOLIDAY_URL",
                value = getLocalProperty("open.api.dev.holiday.url"),
                const = true
            )

            buildConfigField(
                type = FieldSpec.Type.STRING,
                name = "OPEN_API_HOLIDAY_KEY",
                value = getLocalProperty("open.api.dev.holiday.key"),
                const = true
            )
        }
    }

    targetConfigs("real") {
        create("nonAndroid") {
            buildConfigField(
                type = FieldSpec.Type.STRING,
                name = "OPEN_API_HOLIDAY_URL",
                value = getLocalProperty("open.api.real.holiday.url"),
                const = true
            )

            buildConfigField(
                type = FieldSpec.Type.STRING,
                name = "OPEN_API_HOLIDAY_KEY",
                value = getLocalProperty("open.api.real.holiday.key"),
                const = true
            )
        }
    }
}
