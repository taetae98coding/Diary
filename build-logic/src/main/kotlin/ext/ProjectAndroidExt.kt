package ext

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType

internal fun Project.withAndroid(
    action: CommonExtension<*, *, *, *, *, *>.() -> Unit,
) {
    val extension = extensions.findByType<ApplicationExtension>() ?: extensions.findByType<LibraryExtension>()
    if (extension == null) {
        println("$displayName doesn't has android extension.")
        return
    }

    action(extension)
}

internal fun Project.withAndroidApplication(
    action: ApplicationExtension.() -> Unit,
) {
    with(
        receiver = extensions.getByType<ApplicationExtension>(),
        block = action,
    )
}
