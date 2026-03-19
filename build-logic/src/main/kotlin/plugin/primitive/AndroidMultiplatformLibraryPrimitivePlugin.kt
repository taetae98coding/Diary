package plugin.primitive

import io.github.taetae98coding.diary.gradle.plugins
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AndroidMultiplatformLibraryPrimitivePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins {
            apply("diary.primitive.multiplatform")
            apply("diary.primitive.android.library")
        }
    }
}
