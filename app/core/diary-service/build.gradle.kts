plugins {
    id("diary.kotlin.multiplatform.common")
    id("diary.koin.common")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":app:core:account-preferences"))
                implementation(project(":common:model"))
                implementation(libs.bundles.ktor.client)

                api(project(":app:core:model"))
                api(project(":common:exception"))
            }
        }
    }
}
