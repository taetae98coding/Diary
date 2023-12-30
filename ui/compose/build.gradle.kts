plugins {
    id("diary.multiplatform")
    id("diary.compose.multiplatform")
}

kotlin {
    sourceSets {
        val nonIosMain = maybeCreate("nonIosMain")

        nonIosMain.dependsOn(commonMain.get())
        androidMain.get().dependsOn(nonIosMain)
        jvmMain.get().dependsOn(nonIosMain)
        jsMain.get().dependsOn(nonIosMain)

        commonMain {
            dependencies {
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
            }
        }

        androidMain {
            dependencies {
                implementation(compose.uiTooling)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.ui.compose"
}
