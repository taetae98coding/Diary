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
            target.swiftPackageConfig("KMPFirebaseCrashlytics") {
                minIos = "26.4"
                dependency {
                    remotePackageVersion(
                        url = uri("https://github.com/firebase/firebase-ios-sdk"),
                        products = { add("FirebaseCrashlytics", exportToKotlin = true) },
                        version = "12.11.0",
                    )
                }
            }
        }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.logger.crashlytics.api)
            }
        }

        androidMain {
            dependencies {
                implementation(project.dependencies.platform(libs.firebase.bom))
                implementation(libs.firebase.crashlytics)
            }
        }
    }
}
