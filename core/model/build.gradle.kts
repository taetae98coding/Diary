plugins {
    alias(libs.plugins.diary.primitive.multiplatform)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.library.kotlinxDatetime)
                api(libs.kotlinx.datetime)
            }
        }
    }
}
