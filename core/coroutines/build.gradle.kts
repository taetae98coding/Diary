plugins {
    id("diary.module")
    id("diary.koin.multiplatform")
}

kotlin {
    sourceSets {
        val supportMain = maybeCreate("supportMain")

        supportMain.dependsOn(commonMain.get())
        iosMain.get().dependsOn(supportMain)
        jvmMain.get().dependsOn(supportMain)

        commonMain {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}
