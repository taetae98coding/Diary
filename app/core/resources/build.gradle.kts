plugins {
    id("diary.android.library")
    id("diary.kotlin.multiplatform.all")
    id("diary.compose")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                api(compose.components.resources)
            }
        }
    }
}

compose {
    resources {
        publicResClass = true
        packageOfResClass = "${Build.NAMESPACE}.core.resources"
    }
}

android {
    namespace = "${Build.NAMESPACE}.core.resouces"
}
