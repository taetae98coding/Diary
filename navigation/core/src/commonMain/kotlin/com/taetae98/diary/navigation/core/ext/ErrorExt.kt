package com.taetae98.diary.navigation.core.ext

import com.taetae98.diary.navigation.core.route.Route

internal fun illegalRoute(route: Route): Nothing {
    error("Illegal Route : $route")
}