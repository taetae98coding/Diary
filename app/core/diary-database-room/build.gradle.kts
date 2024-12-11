plugins {
    id("diary.room")
    id("diary.koin.room")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":app:core:diary-database"))
                implementation(project(":library:coroutines"))
                implementation(project(":library:room"))
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.room.testing)
            }
        }

        androidUnitTest {
            dependencies {
                implementation(libs.test.core)
                implementation(libs.roboletric)
            }
        }
    }
}

android {
    namespace = "${Build.NAMESPACE}.core.diary.database.room"

    sourceSets {
        getByName("test") {
            assets.srcDir("$projectDir/schemas")
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}
