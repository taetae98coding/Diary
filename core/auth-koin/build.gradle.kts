plugins {
    id("diary.multiplatform")
    id("diary.koin.multiplatform")
}

kotlin {
    sourceSets {
        val nonSupportTargetMain = maybeCreate("nonSupportTargetMain")

        nonSupportTargetMain.dependsOn(commonMain.get())
        jvmMain.get().dependsOn(nonSupportTargetMain)
        jsMain.get().dependsOn(nonSupportTargetMain)

        commonMain {
            dependencies {
                implementation(project(":core:auth-impl"))
                implementation(project(":library:firebase-auth-impl"))
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.core.auth.koin"
}
