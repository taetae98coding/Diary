plugins {
    id("diary.server.data")
}

dependencies {
    implementation(project(":server:core:database"))
    implementation(project(":server:domain:buddy"))

    implementation(platform(libs.exposed.bom))
    implementation(libs.exposed.core)
}