plugins {
    id("diary.multiplatform")
    id("diary.koin.multiplatform")
    alias(libs.plugins.sqldelight)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:coroutines"))

                implementation(project(":data:dto"))
                implementation(project(":data:local-api"))

                implementation(project(":library:kotlin-ext"))

                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
                implementation(libs.sqldelight.adapter)
                implementation(libs.sqldelight.coroutines)
                implementation(libs.sqldelight.paging)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.sqldelight.android.driver)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.sqldelight.native.driver)
                implementation(libs.stately.collections)
            }
        }

        jvmMain {
            dependencies {
                implementation(libs.sqldelight.sqlite.driver)
            }
        }

        jsMain {
            dependencies {
                implementation(libs.stately.collections)
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
            generateAsync.set(true)
//            verifyMigrations.set(true)

            dialect(libs.sqldelight.dialect)
        }
    }
}
