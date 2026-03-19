plugins {
    alias(libs.plugins.diary.primitive.multiplatform)
    alias(libs.plugins.diary.primitive.jvm.test)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.library.kotlinxCoroutinesCore)
                api(projects.core.model)
                api(libs.kotlinx.datetime)
            }
        }
    }
}
