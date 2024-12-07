plugins {
    id("diary.server.data")
}

dependencies {
    implementation(project(":server:core:database"))
    implementation(project(":server:domain:fcm"))

    implementation(libs.server.firebase.admin)

    implementation(platform(libs.exposed.bom))
    implementation(libs.exposed.core)
}
