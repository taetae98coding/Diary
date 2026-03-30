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
                implementation(projects.core.weatherNetwork.api)
            }
        }
    }
}
