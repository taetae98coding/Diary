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
    }
}