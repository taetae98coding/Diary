plugins {
    id("diary.multiplatform")
    id("diary.koin.multiplatform")
}

kotlin {
    sourceSets {
        val supportMain = maybeCreate("supportMain")
        val nonSupportMain = maybeCreate("nonSupportMain")

        supportMain.dependsOn(commonMain.get())
        nonSupportMain.dependsOn(commonMain.get())

        androidMain.get().dependsOn(supportMain)
        iosMain.get().dependsOn(supportMain)
        jvmMain.get().dependsOn(supportMain)
        jsMain.get().dependsOn(nonSupportMain)

        commonMain {
            dependencies {
                implementation(project(":data:pref-api"))

                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
            }
        }

        supportMain.dependencies {
            implementation(libs.datastore.core)
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.data.pref.impl"
}
