plugins {
    id("diary.multiplatform")
}

kotlin {
    sourceSets {
        val nonAndroidMain = maybeCreate("nonAndroidMain")
        val nonSupportMain = maybeCreate("nonSupportMain")

        nonAndroidMain.dependsOn(commonMain.get())
        nonSupportMain.dependsOn(commonMain.get())

        iosMain.get().dependsOn(nonAndroidMain)
        jvmMain.get().dependsOn(nonSupportMain)
        jsMain.get().dependsOn(nonSupportMain)

        commonMain {
            dependencies {
                implementation(libs.kotlinx.serialization)
                implementation(libs.kotlinx.datetime)

                api(project(":library:firebase-firestore-api"))
            }
        }

        androidMain {
            dependencies {
                implementation(project.dependencies.platform(libs.firebase.android.bom))
                implementation(libs.firebase.android.firestore)
            }
        }

        nonAndroidMain.dependencies {
            implementation(libs.firebase.multiplatform.firestore)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.library.firebase.firestore.impl"
}
