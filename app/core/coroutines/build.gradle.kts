plugins {
    id("diary.android.library")
    id("diary.kotlin.multiplatform.all")
    id("diary.koin.all")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                api(libs.lifecycle.common)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.lifecycle.process)
            }
        }

        val nonAndroidMain = create("nonAndroidMain") {
            dependencies {
                implementation(libs.lifecycle.runtime)
            }
        }

        nonAndroidMain.dependsOn(commonMain.get())
        jvmMain.get().dependsOn(nonAndroidMain)
        iosMain.get().dependsOn(nonAndroidMain)
        macosMain.get().dependsOn(nonAndroidMain)
    }
}

android {
    namespace = "${Build.NAMESPACE}.core.coroutines"
}
