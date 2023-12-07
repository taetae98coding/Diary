plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.kotlin.gradle)
    compileOnly(libs.android.gradle)
}

kotlin {
    explicitApi()
}

gradlePlugin {
    plugins {
        register("diary.android.app") {
            id = "diary.android.app"
            implementationClass = "plugin.android.AndroidAppPlugin"
        }
    }
}