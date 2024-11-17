package ext

import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.provider.Provider

internal fun DependencyHandler.implementation(
    dependencyNotation: Provider<MinimalExternalModuleDependency>,
) {
    add("implementation", dependencyNotation)
}

internal fun DependencyHandler.implementation(
    dependencyNotation: ProjectDependency,
) {
    add("implementation", dependencyNotation)
}

internal fun DependencyHandler.ksp(
    dependencyNotation: Provider<MinimalExternalModuleDependency>,
) {
    add("ksp", dependencyNotation)
}

internal fun DependencyHandler.kspJvm(
    dependencyNotation: Provider<MinimalExternalModuleDependency>,
) {
    add("kspJvm", dependencyNotation)
}

internal fun DependencyHandler.kspAndroid(
    dependencyNotation: Provider<MinimalExternalModuleDependency>,
) {
    add("kspAndroid", dependencyNotation)
}

internal fun DependencyHandler.kspIos(
    dependencyNotation: Provider<MinimalExternalModuleDependency>,
) {
    add("kspIosX64", dependencyNotation)
    add("kspIosArm64", dependencyNotation)
    add("kspIosSimulatorArm64", dependencyNotation)
}

public fun DependencyHandler.kspCommon(
    dependencyNotation: Provider<MinimalExternalModuleDependency>,
) {
    kspJvm(dependencyNotation)
    kspIos(dependencyNotation)
}

public fun DependencyHandler.kspAll(
    dependencyNotation: Provider<MinimalExternalModuleDependency>,
) {
    kspJvm(dependencyNotation)
    kspAndroid(dependencyNotation)
    kspIos(dependencyNotation)
}
