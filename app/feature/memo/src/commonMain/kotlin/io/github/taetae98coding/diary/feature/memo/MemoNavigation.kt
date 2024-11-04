package io.github.taetae98coding.diary.feature.memo

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.github.taetae98coding.diary.core.navigation.memo.MemoAddDestination
import io.github.taetae98coding.diary.core.navigation.memo.MemoDestination
import io.github.taetae98coding.diary.core.navigation.memo.MemoDetailDestination
import io.github.taetae98coding.diary.feature.memo.add.MemoAddRoute
import io.github.taetae98coding.diary.feature.memo.detail.MemoDetailRoute
import io.github.taetae98coding.diary.library.navigation.LocalDateNavType
import kotlin.reflect.typeOf
import kotlinx.datetime.LocalDate

public fun NavGraphBuilder.memoNavigation(
    navController: NavController,
) {
    navigation<MemoDestination>(
        startDestination = MemoAddDestination(),
    ) {
        composable<MemoAddDestination>(
            typeMap = mapOf(typeOf<LocalDate?>() to LocalDateNavType),
        ) {
            MemoAddRoute(
                navigateUp = navController::popBackStack,
            )
        }

        composable<MemoDetailDestination> {
            MemoDetailRoute(
                navigateUp = navController::popBackStack,
            )
        }
    }
}
