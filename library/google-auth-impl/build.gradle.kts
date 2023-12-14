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
        val nonSupportTargetMain = maybeCreate("nonSupportTargetMain")

        nonSupportTargetMain.dependsOn(commonMain.get())
        jvmMain.get().dependsOn(nonSupportTargetMain)
        jsMain.get().dependsOn(nonSupportTargetMain)

        commonMain {
            dependencies {
                implementation(project(":library:google-auth-api"))
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
    namespace = "${Build.NAMESPACE}.library.google.auth.impl"

    buildTypes {
        debug {
            buildConfigField(
                type = "String",
                name = "GOOGLE_SERVER_CLIENT_ID",
                value = "\"${getLocalProperty("google.debug.server.client.id")}\""
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

buildkonfig {
    packageName = "${Build.NAMESPACE}.library.google.oauth"

    defaultConfigs {

    }

    targetConfigs("debug") {
        create("ios") {
            buildConfigField(
                type = FieldSpec.Type.STRING,
                name = "GOOGLE_SERVER_CLIENT_ID",
                value = getLocalProperty("google.debug.server.client.id"),
                const = true
            )

            buildConfigField(
                type = FieldSpec.Type.STRING,
                name = "GOOGLE_CLIENT_ID",
                value = getLocalProperty("google.debug.ios.client.id"),
                const = true
            )
        }
    }
}