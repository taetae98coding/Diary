plugins {
    id("diary.server.domain")
}

dependencies {
    implementation(project(":server:domain:account"))
    implementation(project(":server:domain:fcm"))
}
