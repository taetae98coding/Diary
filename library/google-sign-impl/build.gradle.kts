import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.compose.internal.utils.getLocalProperty

plugins {
    id("diary.multiplatform")
    alias(libs.plugins.kotlin.cocoapods)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    cocoapods {
        summary = "Google Auth Implementation"
        homepage = "https://github.com/taetae98coding/Diary"
        version = "1.0.0"
        ios.deploymentTarget = "17.0"

        pod("GoogleSignIn", "7.0.0")
    }

    sourceSets {
        val nonSupportMain = maybeCreate("nonSupportMain")

        nonSupportMain.dependsOn(commonMain.get())
        jvmMain.get().dependsOn(nonSupportMain)
        jsMain.get().dependsOn(nonSupportMain)

        commonMain {
            dependencies {
                implementation(project(":library:google-sign-api"))
            }
        }

        androidMain {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)

                implementation(libs.androidx.core)
                implementation(libs.androidx.credentials)

                // Android 13 and below.
                implementation(libs.androidx.credentials.play.services.auth)

                implementation(libs.google.id)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.library.google.sign.impl"

    productFlavors {
        maybeCreate("dev")
        getByName("dev") {
            buildConfigField(
                type = "String",
                name = "GOOGLE_SERVER_CLIENT_ID",
                value = "\"${getLocalProperty("google.dev.server.client.id")}\""
            )
        }

        maybeCreate("real")
        getByName("real") {
            buildConfigField(
                type = "String",
                name = "GOOGLE_SERVER_CLIENT_ID",
                value = "\"${getLocalProperty("google.real.server.client.id")}\""
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

buildkonfig {
    packageName = "${Build.NAMESPACE}.library.google.sign.impl"

    defaultConfigs {

    }

    targetConfigs("dev") {
        create("ios") {
            buildConfigField(
                type = FieldSpec.Type.STRING,
                name = "GOOGLE_SERVER_CLIENT_ID",
                value = getLocalProperty("google.dev.server.client.id"),
                const = true
            )

            buildConfigField(
                type = FieldSpec.Type.STRING,
                name = "GOOGLE_CLIENT_ID",
                value = getLocalProperty("google.dev.ios.client.id"),
                const = true
            )
        }
    }

    targetConfigs("real") {
        create("ios") {
            buildConfigField(
                type = FieldSpec.Type.STRING,
                name = "GOOGLE_SERVER_CLIENT_ID",
                value = getLocalProperty("google.real.server.client.id"),
                const = true
            )

            buildConfigField(
                type = FieldSpec.Type.STRING,
                name = "GOOGLE_CLIENT_ID",
                value = getLocalProperty("google.real.ios.client.id"),
                const = true
            )
        }
    }
}