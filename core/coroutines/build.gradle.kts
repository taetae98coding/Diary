plugins {
    id("diary.multiplatform")
    id("diary.koin.multiplatform")
}

kotlin {
    sourceSets {
        val nonAndroidMain = maybeCreate("nonAndroidMain")
        val nonJsMain = maybeCreate("nonJsMain")

        nonAndroidMain.dependsOn(commonMain.get())
        nonJsMain.dependsOn(commonMain.get())

        androidMain.get().dependsOn(nonJsMain)

        iosMain.get().dependsOn(nonJsMain)
        iosMain.get().dependsOn(nonAndroidMain)

        jvmMain.get().dependsOn(nonJsMain)
        jvmMain.get().dependsOn(nonAndroidMain)

        jsMain.get().dependsOn(nonAndroidMain)

        commonMain {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.lifecycle.process)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.core.coroutines"
}
