plugins {
    id("diary.multiplatform")
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

                api(project(":library:firebase-auth-api"))
            }
        }

        androidMain {
            dependencies {
                implementation(project.dependencies.platform(libs.firebase.android.bom))
                implementation(libs.firebase.android.auth)
            }
        }

        nonAndroidMain.dependencies {
            implementation(libs.firebase.multiplatform.auth)
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.library.firebase.auth.impl"
}
