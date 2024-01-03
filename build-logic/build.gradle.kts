plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.kotlin.gradle)
    compileOnly(libs.android.gradle)
}

kotlin {
    jvmToolchain(17)
    explicitApi()
}

gradlePlugin {
    plugins {
        register("diary.android.app") {
            id = "diary.android.app"
            implementationClass = "plugin.android.AndroidAppPlugin"
        }

        register("diary.ios") {
            id = "diary.ios"
            implementationClass = "plugin.ios.IosPlugin"
        }

        register("diary.jvm") {
            id = "diary.jvm"
            implementationClass = "plugin.jvm.JvmPlugin"
        }

        register("diary.js") {
            id = "diary.js"
            implementationClass = "plugin.js.JsPlugin"
        }

        register("diary.module") {
            id = "diary.module"
            implementationClass = "plugin.diary.DiaryModulePlugin"
        }

        register("diary.multiplatform") {
            id = "diary.multiplatform"
            implementationClass = "plugin.diary.DiaryMultiplatformPlugin"
        }

        register("diary.compose.multiplatform") {
            id = "diary.compose.multiplatform"
            implementationClass = "plugin.kotlin.ComposeMultiplatformPlugin"
        }

        register("diary.koin.multiplatform") {
            id = "diary.koin.multiplatform"
            implementationClass = "plugin.kotlin.KoinMultiplatformPlugin"
        }

        register("diary.kotest.multiplatform") {
            id = "diary.kotest.multiplatform"
            implementationClass = "plugin.kotlin.KotestMultiplatformPlugin"
        }
    }
}
