package ext

import org.gradle.api.artifacts.dsl.DependencyHandler

internal fun DependencyHandler.kspCommonMain(dependencyNotation: Any) {
    add("kspCommonMainMetadata", dependencyNotation)
}
