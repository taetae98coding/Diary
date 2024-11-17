plugins {
    id("diary.android.library")
    id("diary.kotlin.multiplatform.all")
    id("diary.cocoapods")
}

kotlin {
    cocoapods {
        ios.deploymentTarget = "18.0"

        noPodspec()
        pod(
            name = "FirebaseMessaging",
            version = property("firebase.cocoapods.version") as String,
        )
    }

    sourceSets {
        commonMain {
            dependencies {
                api(project(":library:firebase-common"))
            }
        }

        androidMain {
            dependencies {
                implementation(project.dependencies.platform(libs.android.firebase.bom))
                implementation(libs.android.firebase.messaging)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
            }
        }

        val nonSupportMain = create("nonSupportMain")

        nonSupportMain.dependsOn(commonMain.get())
        jvmMain.get().dependsOn(nonSupportMain)
    }
}

android {
    namespace = "${Build.NAMESPACE}.library.firebase.messaging"
}
