package ext

import androidx.room.gradle.RoomExtension
import com.google.devtools.ksp.gradle.KspExtension
import java.util.Properties
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.PluginContainer
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.HasConfigurableKotlinCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

internal fun Project.withPlugin(
    action: PluginContainer.() -> Unit,
) {
    with(
        receiver = plugins,
        block = action,
    )
}

internal fun Project.withKotlin(
    action: KotlinProjectExtension.() -> Unit,
) {
    with(
        receiver = extensions.getByType<KotlinProjectExtension>(),
        block = action,
    )
}

internal fun Project.withKotlinMultiplatform(
    action: KotlinMultiplatformExtension.() -> Unit,
) {
    with(
        receiver = extensions.getByType<KotlinMultiplatformExtension>(),
        block = action,
    )
}

internal fun Project.withDependency(
    action: DependencyHandler.() -> Unit,
) {
    with(
        receiver = dependencies,
        block = action,
    )
}

internal fun Project.withKsp(
    action: KspExtension.() -> Unit,
) {
    with(
        receiver = extensions.getByType<KspExtension>(),
        block = action,
    )
}

internal fun Project.withRoom(
    action: RoomExtension.() -> Unit,
) {
    with(
        receiver = extensions.getByType<RoomExtension>(),
        block = action,
    )
}

public fun Project.getLocalProperty(): Properties? {
    val file = project.rootProject.file("local.properties")

    return if (file.exists()) {
        Properties().apply {
            file.inputStream()
                .buffered()
                .use { load(it) }
        }
    } else {
        null
    }
}

internal fun KotlinProjectExtension.compilerOptions(
    configure: KotlinCommonCompilerOptions.() -> Unit,
) {
    (this as HasConfigurableKotlinCompilerOptions<*>).compilerOptions(configure)
}
