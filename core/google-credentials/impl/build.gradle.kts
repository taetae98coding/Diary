import io.github.frankois944.spmForKmp.swiftPackageConfig
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.Family

plugins {
    alias(libs.plugins.diary.primitive.multiplatform.android)
    alias(libs.plugins.spm)
}

kotlin {
    targets.withType<KotlinNativeTarget>()
        .filter { it.konanTarget.family == Family.IOS }
        .forEach { target ->
            target.swiftPackageConfig("KMPGoogleSignIn") {
                minIos = "26.4"
                dependency {
                    remotePackageVersion(
                        url = uri("https://github.com/google/GoogleSignIn-iOS"),
                        products = { add("GoogleSignIn", exportToKotlin = true) },
                        version = "9.0.0",
                    )
                }
            }
        }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.core.googleCredentials.api)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.credentials)
                implementation(libs.androidx.credentials.play.services)
                implementation(libs.google.identity)
            }
        }

        appleMain {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
            }
        }

        jvmMain {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.google.oauth.client.jetty)
            }
        }

        wasmJsMain {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}
