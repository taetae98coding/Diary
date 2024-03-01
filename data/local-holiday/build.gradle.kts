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
                implementation(libs.sqldelight.web.driver)
                implementation(libs.stately.collections)

                implementation(devNpm("copy-webpack-plugin", "9.1.0"))
                implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.0.1"))
                implementation(npm("sql.js", "1.8.0"))
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.data.local.holiday"
}

sqldelight {
    databases {
        create("HolidayDatabase") {
            packageName.set("${Build.NAMESPACE}.data.local.holiday")
            schemaOutputDirectory.set(file("src/main/sqldelight/databases"))
            generateAsync.set(true)

            dialect(libs.sqldelight.dialect)
        }
    }
}
