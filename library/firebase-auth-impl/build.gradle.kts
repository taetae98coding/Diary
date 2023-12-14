plugins {
    id("diary.multiplatform")
}

kotlin {
    sourceSets {
        val nonSupportTargetMain = maybeCreate("nonSupportTargetMain")

        nonSupportTargetMain.dependsOn(commonMain.get())
        iosMain.get().dependsOn(nonSupportTargetMain)
        jvmMain.get().dependsOn(nonSupportTargetMain)
        jsMain.get().dependsOn(nonSupportTargetMain)

        commonMain {
            dependencies {
                api(project(":library:firebase-auth-api"))
            }
        }

        androidMain {
            dependencies {
                implementation(project.dependencies.platform(libs.firebase.bom))
                implementation(libs.firebase.auth)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.library.firebase.auth.impl"
}
