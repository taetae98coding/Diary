plugins {
    id("diary.multiplatform")
    id("diary.koin.multiplatform")
    alias(libs.plugins.sqldelight)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":data:dto"))
                implementation(project(":data:local-api"))
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
                implementation(libs.sqldelight.adapter)
                implementation(libs.sqldelight.paging)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.sqldelight.android.driver)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.data.local.impl"
}

sqldelight {
    databases {
        create("DiaryDatabase") {
            packageName.set("${Build.NAMESPACE}.data.local.impl")
            schemaOutputDirectory.set(file("src/main/sqldelight/databases"))
            verifyMigrations.set(true)
            generateAsync.set(true)

            dialect(libs.sqldelight.dialect)
        }
    }
}
