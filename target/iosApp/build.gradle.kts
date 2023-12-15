import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.Family

plugins {
    id("diary.ios")
}

kotlin {
    targets.filterIsInstance<KotlinNativeTarget>()
        .filter { it.konanTarget.family == Family.IOS }
        .forEach { target ->
            target.binaries.framework {
                baseName = "iosApp"
                isStatic = true
            }
        }
}
