plugins {
    id("diary.multiplatform")
    id("diary.compose.multiplatform")
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
                implementation(libs.kotlinx.coroutines.core)
                implementation(compose.runtime)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.lifecycle.compose)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.library.compose.runtime"
}
