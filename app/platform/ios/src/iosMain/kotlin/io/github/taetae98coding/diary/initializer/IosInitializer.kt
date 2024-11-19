package io.github.taetae98coding.diary.initializer

public fun init() {
    initAppLifecycleOwner()
    val koinApplication = initKoin()

    initBackupManager(koinApplication)
    initFetchManager(koinApplication)
    initFirebaseMessagingManager(koinApplication)
}
