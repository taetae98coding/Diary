plugins {
    alias(libs.plugins.diary.primitive.multiplatform)
    alias(libs.plugins.diary.primitive.jvm.test)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.model)
                implementation(projects.core.database.api)
                implementation(projects.core.datastore.api)
                implementation(projects.core.holidayDatabase.api)
                implementation(projects.core.holidayNetwork.api)
                implementation(projects.core.network.api)
                implementation(libs.kotlinx.serialization.json)
                implementation(projects.core.weatherNetwork.api)

                // TODO 한글 URL 변경 시 사용 중 추후 제거
                implementation(libs.ktor.client.core)
            }
        }
    }
}
