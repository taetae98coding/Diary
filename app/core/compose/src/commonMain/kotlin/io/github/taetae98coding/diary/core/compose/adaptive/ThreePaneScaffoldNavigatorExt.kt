package io.github.taetae98coding.diary.core.compose.adaptive

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldValue
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
public fun ThreePaneScaffoldNavigator<*>.isListVisible(): Boolean = scaffoldValue.isListVisible()

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
public fun ThreePaneScaffoldNavigator<*>.isDetailVisible(): Boolean = scaffoldValue.isDetailVisible()

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
public fun ThreePaneScaffoldNavigator<*>.isTertiaryVisible(): Boolean = scaffoldValue.isTertiaryVisible()

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
public fun ThreePaneScaffoldValue.isListVisible(): Boolean = secondary == PaneAdaptedValue.Expanded

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
public fun ThreePaneScaffoldValue.isDetailVisible(): Boolean = primary == PaneAdaptedValue.Expanded

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
public fun ThreePaneScaffoldValue.isTertiaryVisible(): Boolean = tertiary == PaneAdaptedValue.Expanded
