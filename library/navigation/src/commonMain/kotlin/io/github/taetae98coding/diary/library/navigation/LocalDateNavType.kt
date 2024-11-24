package io.github.taetae98coding.diary.library.navigation

import androidx.core.bundle.Bundle
import androidx.navigation.NavType
import kotlinx.datetime.LocalDate

public data object LocalDateNavType : NavType<LocalDate>(true) {
	override fun get(bundle: Bundle, key: String): LocalDate? = bundle.getString(key)?.let { LocalDate.parse(it) }

	override fun parseValue(value: String): LocalDate = LocalDate.parse(value)

	override fun put(bundle: Bundle, key: String, value: LocalDate) {
		bundle.putString(key, value.toString())
	}
}
