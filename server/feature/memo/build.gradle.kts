plugins {
    id("diary.server.feature")
}

dependencies {
    implementation(project(":server:domain:memo"))
}
