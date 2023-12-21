plugins {
    id("diary.module")
    id("diary.koin.multiplatform")
    alias(libs.plugins.kotest)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":domain:exception"))
                implementation(project(":domain:repository"))

                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.paging.common)

                api(project(":domain:entity"))
            }
        }

        commonTest {
            dependencies {
                implementation(libs.bundles.kotest)
            }
        }

        jvmTest {
            dependencies {
                implementation(libs.mockk)

                runtimeOnly(libs.kotest.junit5)
            }
        }
    }
}

tasks.named<Test>("jvmTest") {
    useJUnitPlatform()
}